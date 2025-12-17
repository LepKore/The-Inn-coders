package com.example.laposada

import android.content.Context
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
        // TODO: implementar el detalle
    } }

    companion object {
        val GAME_DATABASE_NAME = "GAME_DATABASE_NAME"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityGamesMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gameDatabase = Room.databaseBuilder(
            context, GameDatabase::class.java, GAME_DATABASE_NAME
        )
        daoGame = gameDatabase.build().DaoGame()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        lifecycleScope.launch {
            guardarDatosBD()
        }

        setupRecyclerView()
        setupListeners()

        lifecycleScope.launch {
            val games = withContext(Dispatchers.IO) { obtenerDatosEnBaseDeDatos() }
            adapter.addDataCards(games)
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

    private suspend fun guardarDatosBD() {
        val gameList = listOf(
            GameDataClass(nombre = "SUSHI GO", imagen = R.drawable.sushigo2),
            GameDataClass(nombre = "SABOTEUR", imagen = R.drawable.saboteur2),
            GameDataClass(nombre = "TANXI", imagen = R.drawable.tanxi2),
            GameDataClass(nombre = "COFFEE RUSH", imagen = R.drawable.coffer2),
            GameDataClass(nombre = "SOVIET KITCHEN", imagen = R.drawable.soviet2)
        )

        val currentGames = daoGame.getAll()
        if (currentGames.isEmpty()) {
            daoGame.insertAll(gameList)
        }
    }

    private suspend fun obtenerDatosEnBaseDeDatos(): List<GameDataClass> {
        return daoGame.getAll()
    }
}