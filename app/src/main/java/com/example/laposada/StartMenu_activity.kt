package com.example.laposada

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.laposada.databinding.ActivityStartMenuBinding

class StartMenu_activity : AppCompatActivity() {

    private lateinit var binding: ActivityStartMenuBinding
    val context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityStartMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
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