package com.example.laposada

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Selection_Of_Tables : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_selection_of_tables)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val botonMapa: Button = findViewById(R.id.button_mapButton)

        botonMapa.setOnClickListener {
            // Intent para ir de esta Activity a Map_Tables
            val intent = Intent(this, Map_Tables::class.java)
            startActivity(intent)
        }

        // Asignar el botón de la mesa 1
        val mesa1Button: Button = findViewById(R.id.mesa1)
        mesa1Button.setOnClickListener {
            val intent = Intent(this, reserve_selection::class.java)
            intent.putExtra("mesaNumero", "Mesa #1") // Pasamos el número de mesa
            startActivity(intent)
        }

        // Asignar el botón de la mesa 2
        val mesa2Button: Button = findViewById(R.id.mesa2)
        mesa2Button.setOnClickListener {
            val intent = Intent(this, reserve_selection::class.java)
            intent.putExtra("mesaNumero", "Mesa #2") // Pasamos el número de mesa
            startActivity(intent)
        }

        // Asignar el botón de la mesa 3
        val mesa3Button: Button = findViewById(R.id.mesa3)
        mesa3Button.setOnClickListener {
            val intent = Intent(this, reserve_selection::class.java)
            intent.putExtra("mesaNumero", "Mesa #3") // Pasamos el número de mesa
            startActivity(intent)
        }

        // Asignar el botón de la mesa 4
        val mesa4Button: Button = findViewById(R.id.mesa4)
        mesa3Button.setOnClickListener {
            val intent = Intent(this, reserve_selection::class.java)
            intent.putExtra("mesaNumero", "Mesa #4") // Pasamos el número de mesa
            startActivity(intent)
        }

        // Asignar el botón de la mesa 5
        val mesa5Button: Button = findViewById(R.id.mesa5)
        mesa3Button.setOnClickListener {
            val intent = Intent(this, reserve_selection::class.java)
            intent.putExtra("mesaNumero", "Mesa #5") // Pasamos el número de mesa
            startActivity(intent)
        }

        // Asignar el botón de la mesa 6
        val mesa6Button: Button = findViewById(R.id.mesa6)
        mesa3Button.setOnClickListener {
            val intent = Intent(this, reserve_selection::class.java)
            intent.putExtra("mesaNumero", "Mesa #6") // Pasamos el número de mesa
            startActivity(intent)
        }

        val backButton: Button = findViewById(R.id.button_backButton)
        backButton.setOnClickListener {
            onBackPressed()  // Esto redirige a la actividad anterior
        }

    }
}