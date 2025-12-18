package com.example.laposada.pantallas.startMenu

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.laposada.R
import com.example.laposada.databinding.ActivityStartMenuBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class StartMenu_activity : AppCompatActivity() {

    private lateinit var binding: ActivityStartMenuBinding
    private lateinit var auth: FirebaseAuth
    val context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityStartMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val currentUser = auth.currentUser

        if (currentUser != null) {
            val intentUsuarioLogueado = Intent(context, HomeMenu_Activity::class.java)
            startActivity(intentUsuarioLogueado)

        }
        irLogin()
        irRegister()
    }

    fun irLogin() {
        binding.buttonLogin.setOnClickListener {
            val intent = Intent(context, Login_activity::class.java)
            startActivity(intent)
        }
    }

    fun irRegister() {
        binding.buttonRegister.setOnClickListener {
            val intent = Intent(context, Register_activity::class.java)
            startActivity(intent)
        }
    }
}