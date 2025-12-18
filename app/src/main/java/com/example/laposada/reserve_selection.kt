package com.example.laposada

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.laposada.dataBase.GameDatabase

class reserve_selection : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reserve_selection)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val mesaNumero = intent.getStringExtra("mesaNumero")

        val mesaTitle: TextView = findViewById(R.id.titulo_de_mesa)
        mesaTitle.text = mesaNumero // Se actualizará con el número de la mesa seleccionada

        val spInicio = findViewById<Spinner>(R.id.spinner_hora_inicio)
        val spFin = findViewById<Spinner>(R.id.spinner_hora_fin)

        val horasInicio = horasRango(15, 21)
        val adapterInicio = ArrayAdapter(this, android.R.layout.simple_spinner_item, horasInicio).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spInicio.adapter = adapterInicio

        fun actualizarSpinnerFin() {
            val horaInicioSeleccionada = indiceDeHora(spInicio.selectedItem.toString())
            val horasFin = horasRango(horaInicioSeleccionada, 23)

            val adapterFin = ArrayAdapter(this, android.R.layout.simple_spinner_item, horasFin).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
            spFin.adapter = adapterFin
            spFin.setSelection(0)
        }

        actualizarSpinnerFin()

        spInicio.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: android.widget.AdapterView<*>?,
                view: android.view.View?,
                position: Int,
                id: Long
            ) {
                actualizarSpinnerFin()
            }

            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}

        }

        val spinnerJuego = findViewById<Spinner>(R.id.spinner_juego)

        val juegos = listOf(
            "Sushi Go",
            "Catan",
            "Uno",
            "Exploding Kittens",
            "Carcassonne",
            "Coffee Rush"
        )

        val adapterJuego = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            juegos
        )

        adapterJuego.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spinnerJuego.adapter = adapterJuego

        val btnBack = findViewById<Button>(R.id.button_backButton)
        btnBack.setOnClickListener {
            finish()
        }

    }

    fun horasRango(inicio: Int, fin: Int): List<String> {
        val lista = mutableListOf<String>()
        for (h in inicio..fin) {
            lista.add(String.format("%02d:00", h))
        }
        return lista
    }

    fun indiceDeHora(texto: String): Int {
        return texto.substring(0, 2).toInt()
    }

}