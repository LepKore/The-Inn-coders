package com.example.laposada

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.laposada.adapters.GameSpinnerAdapter
import com.example.laposada.dataBase.DaoGame
import com.example.laposada.dataBase.GameDatabase
import com.example.laposada.dataClass.GameDataClass
import com.example.laposada.databinding.ActivityDeleteGameBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DeleteGameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDeleteGameBinding
    private val context = this
    lateinit var daoGame: DaoGame
    private val gameList: MutableList<GameDataClass> = mutableListOf()

    private val spinnerGameAdapter: GameSpinnerAdapter by lazy {
        GameSpinnerAdapter(this, gameList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDeleteGameBinding.inflate(layoutInflater)
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

        binding.spinnerGame.adapter = spinnerGameAdapter
        loadGames()
        setupListeners()
    }

    private fun setupListeners() {
        binding.buttonBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.buttonDelete.setOnClickListener {
            deleteSelectedGame()
        }
    }

    private fun loadGames() {
        lifecycleScope.launch {
            val games = withContext(Dispatchers.IO) {
                daoGame.getAll()
            }
            gameList.clear()
            gameList.addAll(games)
            if (gameList.isEmpty()) {
                Toast.makeText(
                    this@DeleteGameActivity,
                    "No hay juegos",
                    Toast.LENGTH_SHORT
                ).show()
            }
            spinnerGameAdapter.notifyDataSetChanged()
        }
    }

    private fun deleteSelectedGame() {
        if (gameList.isEmpty()) {
            Toast.makeText(this, "No hay juegos seleccionados", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedGame = binding.spinnerGame.selectedItem as GameDataClass

        lifecycleScope.launch(Dispatchers.IO) {
            daoGame.deleteById(selectedGame.id)

            withContext(Dispatchers.Main) {
                Toast.makeText(
                    this@DeleteGameActivity,
                    "Juego eliminado",
                    Toast.LENGTH_SHORT
                ).show()
                loadGames()
            }
        }
    }
}