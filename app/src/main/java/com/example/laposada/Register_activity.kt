package com.example.laposada

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.laposada.databinding.ActivityRegisterBinding

class Register_activity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    val context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        irStart()
        confirmarPassword()
    }

    fun irStart() {
        binding.buttonBackButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    fun confirmarPassword() {
        binding.buttonSend.setOnClickListener {
            // TODO
            // Implementacion de cambio a home y validacion de password
            val intent = Intent(context, Login_activity::class.java)
            startActivity(intent)
        }

    }

}