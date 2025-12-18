package com.example.laposada.pantallas.mesas

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.laposada.R
import com.example.laposada.reserve_selection

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
            val intent = Intent(this, Map_Tables::class.java)
            startActivity(intent)
        }

        val mesa1Button: Button = findViewById(R.id.mesa1)
        mesa1Button.setOnClickListener {
            val intent = Intent(this, reserve_selection::class.java)
            intent.putExtra("mesaNumero", "Mesa #1")
            startActivity(intent)
        }

        val mesa2Button: Button = findViewById(R.id.mesa2)
        mesa2Button.setOnClickListener {
            val intent = Intent(this, reserve_selection::class.java)
            intent.putExtra("mesaNumero", "Mesa #2")
            startActivity(intent)
        }

        val mesa3Button: Button = findViewById(R.id.mesa3)
        mesa3Button.setOnClickListener {
            val intent = Intent(this, reserve_selection::class.java)
            intent.putExtra("mesaNumero", "Mesa #3")
            startActivity(intent)
        }

        val mesa4Button: Button = findViewById(R.id.mesa4)
        mesa4Button.setOnClickListener {
            val intent = Intent(this, reserve_selection::class.java)
            intent.putExtra("mesaNumero", "Mesa #4")
            startActivity(intent)
        }

        val mesa5Button: Button = findViewById(R.id.mesa5)
        mesa5Button.setOnClickListener {
            val intent = Intent(this, reserve_selection::class.java)
            intent.putExtra("mesaNumero", "Mesa #5")
            startActivity(intent)
        }

        val mesa6Button: Button = findViewById(R.id.mesa6)
        mesa6Button.setOnClickListener {
            val intent = Intent(this, reserve_selection::class.java)
            intent.putExtra("mesaNumero", "Mesa #6")
            startActivity(intent)
        }

        val backButton: Button = findViewById(R.id.button_backButton)
        backButton.setOnClickListener {
            onBackPressed()
        }

    }
}