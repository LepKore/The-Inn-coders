package com.example.laposada.pantallas.menuJuegos

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.laposada.R
import com.example.laposada.adapters.LabelAdapter
import com.example.laposada.dataBase.DaoGame
import com.example.laposada.dataBase.DaoGameType
import com.example.laposada.dataBase.GeneralDataBase
import com.example.laposada.dataClass.GameDataClass
import com.example.laposada.databinding.ActivityGameDescriptionBinding
import com.example.laposada.pantallas.menuComida.FoodMenuActivity.Companion.DATABASE_NAME
import com.example.laposada.pantallas.menuJuegos.GamesMenuActivity.Companion.GAME_ID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class GameDescriptionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameDescriptionBinding
    private val context = this
    private lateinit var daoGame: DaoGame
    private lateinit var daoGameType: DaoGameType

    private val adapter: LabelAdapter by lazy { LabelAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityGameDescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dataBase = Room.databaseBuilder(
            context,
            GeneralDataBase::class.java,
            DATABASE_NAME
        )
//            .fallbackToDestructiveMigration()
            .build()

        daoGame     = dataBase.DaoGame()
        daoGameType = dataBase.DaoGameType()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        lifecycleScope.launch {

            val gameId = intent.getIntExtra(GAME_ID, -1)
            val result = withContext(Dispatchers.IO) {
                val food = daoGame.getAll().find { it.id == gameId }

                val tipos = food?.categorias?.map { id ->
                    daoGameType.selectById(id)
                }

                Pair(food, tipos)
            }

            val game = result.first
            val tipos = result.second
            game?.let { f ->
                val imageFileCaja = File(filesDir, f.imagenCaja)
                val imageFileGame = File(filesDir, f.imagenGame)
                if (imageFileCaja.exists()) {
                    binding.imageGame1.background =
                        Drawable.createFromPath(imageFileCaja.path)
                }
                if (imageFileGame.exists()) {
                    binding.imageGame2.background =
                        Drawable.createFromPath(imageFileGame.path)
                }

                binding.textViewGameName.text = f.nombre
                binding.textDescription.text = f.descripcion

                tipos?.let {
                    adapter.addDataCards(it)
                }
            }

            setupRecyclerView()
            setupListeners()
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerViewTipos.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewTipos.adapter = adapter
    }

    private fun setupListeners() {
        binding.buttonBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}