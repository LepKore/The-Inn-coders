package com.example.laposada.pantallas.menuJuegos

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.room.Room
import com.example.laposada.R
import com.example.laposada.adapters.GameAdapter
import com.example.laposada.dataBase.DaoGame
import com.example.laposada.dataBase.GeneralDataBase
import com.example.laposada.dataClass.FoodDataClass
import com.example.laposada.dataClass.GameDataClass
import com.example.laposada.databinding.ActivityGamesMenuBinding
import com.example.laposada.pantallas.menuComida.FoodDescriptionActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GamesMenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGamesMenuBinding
    private val context = this
    private lateinit var daoGame: DaoGame
    private val adapter: GameAdapter by lazy {
        GameAdapter { game ->
            // TODO: implementar el detalle
        }
    }

    companion object {
        val GAME_DATABASE_NAME = "GAME_DATABASE_NAME"
        val GAME_ID = "GAME_ID"
    }

    private val launcherEditMenu = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            lifecycleScope.launch {
                val game = withContext(Dispatchers.IO) {
                    getGameList()
                }
                adapter.addDataCards(game)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityGamesMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dataBase = Room.databaseBuilder(
            context,
            GeneralDataBase::class.java,
            GAME_DATABASE_NAME
        ).build()
        daoGame = dataBase.DaoGame()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupRecyclerView()
        setupListeners()
    }
    override fun onResume() {
        super.onResume()
        drawGames()
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
        val intent = Intent(context, FoodDescriptionActivity::class.java)
        intent.putExtra(GAME_ID, game.id)
        startActivity(intent)
    }

    private suspend fun getGameList():List<GameDataClass>  {
        val game = withContext(Dispatchers.IO) { daoGame.getAll() }
        return game
    }

    private fun drawGames() {
        lifecycleScope.launch {
            val game = getGameList()
            adapter.addDataCards(game)
        }
    }
}