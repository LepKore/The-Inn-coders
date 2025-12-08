package com.example.laposada

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.laposada.databinding.ActivityLoginBinding

class Login_activity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    val context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        irStart()
        irHome()


    }

    fun irStart() {
        binding.buttonBackButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
    fun irHome() {
        binding.buttonSend.setOnClickListener {
            val intent = Intent(context, HomeMenu_Activity::class.java)
            startActivity(intent)
        }
    }
}