package com.example.laposada

import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.laposada.dataBase.DaoGame
import com.example.laposada.dataBase.GameDatabase
import com.example.laposada.dataClass.GameDataClass
import com.example.laposada.databinding.ActivityAddGameBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddGameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddGameBinding
    private val context = this
    lateinit var daoGame: DaoGame

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gameDatabase: GameDatabase = Room.databaseBuilder(
            context, GameDatabase::class.java, "GAME_DATABASE_NAME"
        ).build()
        daoGame = gameDatabase.DaoGame()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupSpinner()
        setupListeners()
    }

    private fun setupSpinner() {
        val categories = listOf("Cartas", "Estrategia", "Cooperativo", "Dados", "Tablero", "Familiar")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerType.adapter = adapter
    }

    private fun setupListeners() {
        binding.buttonBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.buttonAdd.setOnClickListener {
            addGame()
        }
    }

    private fun addGame() {
        val name = binding.inputName.text.toString()
        val description = binding.inputDescription.text.toString()
        val type = binding.spinnerType.selectedItem.toString()

        if (name.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }


        val defaultImage1 = R.drawable.sushigo1
        val defaultImage2 = R.drawable.sushigo2

        val newGame = GameDataClass(
            nombre = name,
            imagen1 = defaultImage1,
            imagen2 = defaultImage2,
            descripcion = description,
            categorias = listOf(type)
        )

        lifecycleScope.launch(Dispatchers.IO) {
            daoGame.insertAll(listOf(newGame))
            withContext(Dispatchers.Main) {
                Toast.makeText(this@AddGameActivity, "Juego añadido correctamente", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}