package com.example.laposada

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.laposada.databinding.ActivityHomeMenuBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class HomeMenu_Activity : AppCompatActivity() {

    val context: Context = this
    private lateinit var binding: ActivityHomeMenuBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityHomeMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        irBack()
        setupListeners()

        // Referencia al botón
        val botonMesas = findViewById<Button>(R.id.boton_mesas)

        // Configurar el OnClickListener para el botón
        botonMesas.setOnClickListener {
            // Crear un Intent para redirigir a otra actividad (ejemplo: Selection_Of_Tables)
            val intent = Intent(this, Selection_Of_Tables::class.java) // Cambia a la actividad de destino
            startActivity(intent)
        }
    }


    fun irBack() {
        binding.buttonBackButton.setOnClickListener {
            auth.signOut()
            onBackPressedDispatcher.onBackPressed()
        }

    }

    private fun setupListeners() {
        binding.cardViewFoodMenu.setOnClickListener {
            val intent = Intent(this, FoodMenuActivity::class.java)
            startActivity(intent)
        }
    }



}