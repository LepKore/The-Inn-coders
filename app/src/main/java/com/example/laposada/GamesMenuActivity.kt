package com.example.laposada

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.room.Room
import com.example.laposada.adapters.GameAdapter
import com.example.laposada.dataBase.DaoGame
import com.example.laposada.dataBase.GameDatabase
import com.example.laposada.dataClass.GameDataClass
import com.example.laposada.databinding.ActivityGamesMenuBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GamesMenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGamesMenuBinding
    private val context = this
    private lateinit var daoGame: DaoGame
    private val adapter: GameAdapter by lazy { GameAdapter { game ->
        openGameDetail(game)
    } }

    companion object {
        val GAME_DATABASE_NAME = "GAME_DATABASE_NAME"
        val GAME_ID = "GAME_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityGamesMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gameDatabase = Room.databaseBuilder(
            context, GameDatabase::class.java, GAME_DATABASE_NAME
        )
        .fallbackToDestructiveMigration()
        .build()
        
        daoGame = gameDatabase.DaoGame()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupRecyclerView()
        setupListeners()

        // Wait for DB population before loading data
        lifecycleScope.launch {
            try {
                // First ensure data is inserted
                guardarDatosBD()
                // Then load the data
                val games = withContext(Dispatchers.IO) { obtenerDatosEnBaseDeDatos() }
                adapter.addDataCards(games)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerViewGames.layoutManager = GridLayoutManager(this, 2)
        binding.recyclerViewGames.adapter = adapter
    }

    private fun setupListeners() {
        binding.buttonBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.buttonEdit.setOnClickListener {
             // TODO: Implement Edit Menu for games
        }
    }
    
    private fun openGameDetail(game: GameDataClass) {
        val intent = Intent(context, GameDescriptionActivity::class.java)
        intent.putExtra(GAME_ID, game.id)
        startActivity(intent)
    }

    private suspend fun guardarDatosBD() {
        val gameList = listOf(
            GameDataClass(
                nombre = "SUSHI GO",
                imagen1 = R.drawable.sushigo2,
                imagen2 = R.drawable.sushigo1,
                descripcion = "Juega con tus amigos una divertida ronda armando variados platos japoneses.\nJuega combinaciones y gana mas puntos que los demas para ganar.",
                categorias = listOf("Cartas", "Corto")
            ),
            GameDataClass(
                nombre = "SABOTEUR",
                imagen1 = R.drawable.saboteur2,
                imagen2 = R.drawable.saboteur1,
                descripcion = "Descripción de Saboteur...",
                categorias = listOf("Estrategia", "Roles Ocultos")
            ),
            GameDataClass(
                nombre = "TANXI",
                imagen1 = R.drawable.tanxi2,
                imagen2 = R.drawable.tanxi1,
                descripcion = "Descripción de Tanxi...",
                categorias = listOf("Estrategia", "Tablero")
            ),
            GameDataClass(
                nombre = "COFFEE RUSH",
                imagen1 = R.drawable.coffer2,
                imagen2 = R.drawable.coffer1,
                descripcion = "Descripción de Coffee Rush...",
                categorias = listOf("Estrategia", "Velocidad")
            ),
            GameDataClass(
                nombre = "SOVIET KITCHEN",
                imagen1 = R.drawable.soviet2,
                imagen2 = R.drawable.soviet1,
                descripcion = "Descripción de Soviet Kitchen...",
                categorias = listOf("Cooperativo", "Cartas")
            )
        )

        withContext(Dispatchers.IO) {
            val currentGames = daoGame.getAll()
            if (currentGames.isEmpty()) {
                daoGame.insertAll(gameList)
            }
        }
    }

    private suspend fun obtenerDatosEnBaseDeDatos(): List<GameDataClass> {
        return daoGame.getAll()
    }
}