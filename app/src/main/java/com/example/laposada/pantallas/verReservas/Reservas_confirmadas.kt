package com.example.laposada.pantallas.verReservas

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.laposada.R
import com.example.laposada.adapters.FoodAdapter
import com.example.laposada.adapters.ReservaAdapter
import com.example.laposada.dataBase.DaoGame
import com.example.laposada.dataBase.DaoReservas
import com.example.laposada.dataBase.GeneralDataBase
import com.example.laposada.dataClass.ReservaDataClass
import com.example.laposada.dataClass.ReservaUI
import com.example.laposada.databinding.ActivityReservasConfirmadasBinding
import com.example.laposada.pantallas.menuComida.FoodMenuActivity.Companion.DATABASE_NAME
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class Reservas_confirmadas : AppCompatActivity() {

    private lateinit var daoReservas: DaoReservas
    private lateinit var daoGames: DaoGame
    private val adapter: ReservaAdapter by lazy {
        ReservaAdapter {}
    }


    private lateinit var binding: ActivityReservasConfirmadasBinding

    private val formatterDia = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val formatterHora = DateTimeFormatter.ofPattern("HH:mm")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityReservasConfirmadasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = Room.databaseBuilder(
            this,
            GeneralDataBase::class.java,
                DATABASE_NAME
        ).build()

        daoReservas = database.DaoReservas()
        daoGames = database.DaoGame()

        setupRecycler()
        cargarReservas()
    }

    private fun setupRecycler() {
        binding.buttonBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.recyclerReservas.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerReservas.adapter = adapter
    }

    private fun cargarReservas() {
        lifecycleScope.launch {
            val ahora = LocalDateTime.now()

            val reservasUI = withContext(Dispatchers.IO) {
                val reservas = daoReservas.getAll().toMutableList()
                val juegos = daoGames.getAll().associateBy { it.id }

                val iterator = reservas.iterator()
                while (iterator.hasNext()) {
                    val reserva = iterator.next()

                    val fechaFin = LocalDateTime.of(
                        LocalDate.parse(reserva.dia, formatterDia),
                        LocalTime.parse(reserva.horaFin, formatterHora)
                    )

                    if (fechaFin.isBefore(ahora)) {
                        daoReservas.delete(reserva)
                        iterator.remove()
                    }
                }

                reservas
                    .sortedBy {
                        LocalDateTime.of(
                            LocalDate.parse(it.dia, formatterDia),
                            LocalTime.parse(it.horaInicio, formatterHora)
                        )
                    }
                    .map { reserva ->
                        ReservaUI(
                            id = reserva.id,
                            mesa = reserva.mesa,
                            dia = reserva.dia,
                            horaInicio = reserva.horaInicio,
                            horaFin = reserva.horaFin,
                            nombreJuego = juegos[reserva.juego]?.nombre ?: "Sin juego"
                        )
                    }
            }

            adapter.addDataCards(reservasUI)
        }
    }
}
