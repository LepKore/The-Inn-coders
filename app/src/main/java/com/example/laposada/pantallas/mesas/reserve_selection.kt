package com.example.laposada

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.laposada.dataBase.DaoFood
import com.example.laposada.dataBase.DaoGame
import com.example.laposada.dataBase.DaoReservas
import com.example.laposada.dataBase.GeneralDataBase
import com.example.laposada.dataClass.ReservaDataClass
import com.example.laposada.databinding.ActivityReserveSelectionBinding
import com.example.laposada.pantallas.menuComida.FoodMenuActivity.Companion.DATABASE_NAME
import com.example.laposada.pantallas.mesas.Selection_Of_Tables.Companion.ID_MESA
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

class reserve_selection : AppCompatActivity() {
    private var mesaNumero: Int? = null
    private var horaInicio: String? = null
    private var horaFin: String? = null
    private var diaEvento: String? = null
    private var horaInicioMinutos: Int? = null
    private var horaFinMinutos: Int? = null

    private var juegoSeleccionado: Int? = null

    lateinit var binding: ActivityReserveSelectionBinding
    lateinit var daoGames: DaoGame
    lateinit var daoReservas: DaoReservas


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityReserveSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
        //    val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
         //   v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
         //   insets
        //}

        val dataBase = Room.databaseBuilder(
            this,
            GeneralDataBase::class.java,
            DATABASE_NAME
        )
//            .fallbackToDestructiveMigration()
            .build()
        daoGames = dataBase.DaoGame()
        daoReservas = dataBase.DaoReservas()


        mesaNumero = intent.getIntExtra(ID_MESA, 1)
        binding.tituloDeMesa.text = "Mesa #$mesaNumero"

        setupListeners()

    }

    private fun setupListeners() {
        binding.buttonBackButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.textViewHoraInicio.setOnClickListener {
            mostrarTimePicker(true)
        }

        binding.textviewHoraFin.setOnClickListener {
            mostrarTimePicker(false)
        }
        binding.textviewDia.setOnClickListener {
            mostrarDatePicker()
        }
        binding.juegoSelc.setOnClickListener {
            mostrarDialogoJuegos()
        }

        binding.btnAceptar.setOnClickListener {
            guardarReserva()
        }

    }

    private fun mostrarTimePicker(esInicio: Boolean) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePicker = TimePickerDialog(
            this,
            { _, h, m ->
                val minutosTotales = h * 60 + m
                val tiempoFormateado = String.format("%02d:%02d", h, m)

                if (esInicio) {
                    horaInicioMinutos = minutosTotales
                    horaInicio = tiempoFormateado
                    binding.textViewHoraInicio.text = tiempoFormateado

                    if (horaFinMinutos != null && horaFinMinutos!! <= minutosTotales) {
                        horaFinMinutos = null
                        horaFin = null
                        binding.textviewHoraFin.text = "Selecciona la hora de fin"
                    }
                } else {
                    if (horaInicioMinutos == null) {
                        Toast.makeText(
                            this,
                            "Primero seleccione la hora de inicio",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@TimePickerDialog
                    }

                    if (minutosTotales <= horaInicioMinutos!!) {
                        Toast.makeText(
                            this,
                            "La hora de fin debe ser mayor a la hora de inicio",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@TimePickerDialog
                    }

                    horaFinMinutos = minutosTotales
                    horaFin = tiempoFormateado
                    binding.textviewHoraFin.text = tiempoFormateado
                }
            },
            hour,
            minute,
            true
        )
        timePicker.show()
    }

    private fun mostrarDialogoJuegos() {

        lifecycleScope.launch {
            val juegos = withContext(Dispatchers.IO) { daoGames.getAll() }

            if (juegos.isEmpty()) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@reserve_selection, "No hay juegos disponibles", Toast.LENGTH_SHORT).show()
                }
                return@launch
            }

            val nombres = juegos.map { it.nombre }.toTypedArray()
            withContext(Dispatchers.Main) {
                AlertDialog.Builder(this@reserve_selection)
                    .setTitle("Seleccione un juego")
                    .setItems(nombres) { _, which ->
                        juegoSeleccionado = juegos[which].id
                        binding.juegoSelc.text = juegos[which].nombre
                    }
                    .setNegativeButton("Cancelar") { dialog, _ -> dialog.dismiss() }
                    .show()
            }
        }
    }

    private fun guardarReserva() {
        if (horaInicio == null || horaFin == null || diaEvento == null) {
            Toast.makeText(this, "Seleccione un valor para cada campo de tiempo", Toast.LENGTH_SHORT).show()
            return
        }

        val reserva = ReservaDataClass(
            id = 0,
            mesa = mesaNumero?: 1,
            horaInicio = horaInicio!!,
            horaFin = horaFin!!,
            dia = diaEvento?: "",
            juego = juegoSeleccionado?: -1
        )


        lifecycleScope.launch(Dispatchers.IO) {
            daoReservas.insert(reserva)
        }

        Toast.makeText(this, "Reserva guardada", Toast.LENGTH_SHORT).show()
    }

    private fun mostrarDatePicker() {
        val calendar = Calendar.getInstance()

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(
            this,
            { _, y, m, d ->
                diaEvento = String.format("%04d-%02d-%02d", y, m + 1, d)
                binding.textviewDia.text =
                    String.format("%02d/%02d/%04d", d, m + 1, y)
            },
            year,
            month,
            day
        )
        datePicker.datePicker.minDate = System.currentTimeMillis()

        datePicker.show()
    }


}

