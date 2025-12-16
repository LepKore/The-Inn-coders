package com.example.laposada

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

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

        // Obtener el número de mesa desde el Intent
        val mesaNumero = intent.getStringExtra("mesaNumero")

        // Asignar el número de mesa al TextView
        val mesaTitle: TextView = findViewById(R.id.titulo_de_mesa)
        mesaTitle.text = mesaNumero // Se actualizará con el número de la mesa seleccionada
    }
}