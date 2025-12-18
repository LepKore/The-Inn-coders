package com.example.laposada.pantallas.mesas

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.laposada.R
import com.example.laposada.databinding.ActivitySelectionOfTablesBinding
import com.example.laposada.reserve_selection

class Selection_Of_Tables : AppCompatActivity() {

    private lateinit var binding: ActivitySelectionOfTablesBinding

    companion object {
        val ID_MESA = "ID_MESA"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySelectionOfTablesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        binding.buttonMapButton.setOnClickListener {
            val intent = Intent(this, Map_Tables::class.java)
            startActivity(intent)
        }

        binding.mesa1.setOnClickListener {
            abrirReserva(1)
        }

        binding.mesa2.setOnClickListener {
            abrirReserva(2)
        }

        binding.mesa3.setOnClickListener {
            abrirReserva(3)
        }

        binding.mesa4.setOnClickListener {
            abrirReserva(4)
        }

        binding.mesa5.setOnClickListener {
            abrirReserva(5)
        }

        binding.mesa6.setOnClickListener {
            abrirReserva(6)
        }

        binding.buttonBackButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

    }
    fun abrirReserva(mesa: Int) {
        val intent = Intent(this, reserve_selection::class.java)
        intent.putExtra(ID_MESA, mesa)
        startActivity(intent)
    }
}