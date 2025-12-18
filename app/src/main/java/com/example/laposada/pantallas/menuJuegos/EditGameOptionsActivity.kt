package com.example.laposada.pantallas.menuJuegos

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.laposada.R
import com.example.laposada.databinding.ActivityEditGameOptionsBinding

class EditGameOptionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditGameOptionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditGameOptionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupListeners()
    }

    private fun setupListeners() {
        binding.buttonBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.buttonAdd

        binding.buttonAdd.setOnClickListener {
            val intent = Intent(this, AddGameActivity::class.java)
            startActivity(intent)
        }

        binding.buttonEdit.setOnClickListener {
            val intent = Intent(this, EditGameActivity::class.java)
            startActivity(intent)
        }

        binding.buttonRemove.setOnClickListener {
            val intent = Intent(this, DeleteGameActivity::class.java)
            startActivity(intent)
        }
    }
}