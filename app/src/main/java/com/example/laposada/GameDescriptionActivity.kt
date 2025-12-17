package com.example.laposada

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.laposada.GamesMenuActivity.Companion.GAME_DATABASE_NAME
import com.example.laposada.GamesMenuActivity.Companion.GAME_ID
import com.example.laposada.dataBase.DaoGame
import com.example.laposada.dataBase.GameDatabase
import com.example.laposada.dataClass.GameDataClass
import com.example.laposada.databinding.ActivityGameDescriptionBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameDescriptionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameDescriptionBinding
    private val context = this
    private lateinit var daoGame: DaoGame

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityGameDescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gameDatabase = Room.databaseBuilder(
            context, GameDatabase::class.java, GAME_DATABASE_NAME
        )
        .build()
        daoGame = gameDatabase.DaoGame()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupBack()


        lifecycleScope.launch {
            val games = withContext(Dispatchers.IO) { daoGame.getAll() }
            val gameId = intent.getIntExtra(GAME_ID, -1)
            val game = games.find { it.id == gameId }

            game?.let { g ->
                loadGameData(g)
            }
        }
    }

    private fun loadGameData(game: GameDataClass) {
        //poner imagenes
        binding.imageGame1.setImageResource(game.imagen1)
        binding.imageGame2.setImageResource(game.imagen2)


        binding.textDescription.text = game.descripcion

        if (game.categorias.isNotEmpty()) {
            binding.textCat1.text = game.categorias[0]
            if (game.categorias.size > 1) {
                binding.textCat2.text = game.categorias[1]
            } else {
                binding.textCat2.text = ""
            }
        }
    }

    private fun setupBack() {
        binding.buttonBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}