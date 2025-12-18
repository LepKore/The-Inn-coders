package com.example.laposada.pantallas.startMenu

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.laposada.pantallas.menuComida.FoodMenuActivity
import com.example.laposada.pantallas.menuJuegos.GamesMenuActivity
import com.example.laposada.R
import com.example.laposada.dataBase.DaoFoodType
import com.example.laposada.dataBase.DaoGameType
import com.example.laposada.dataBase.GeneralDataBase
import com.example.laposada.dataClass.FoodTypeDataClass
import com.example.laposada.dataClass.GameTypeDataClass
import com.example.laposada.databinding.ActivityHomeMenuBinding
import com.example.laposada.pantallas.menuComida.FoodMenuActivity.Companion.DATABASE_NAME
import com.example.laposada.pantallas.mesas.Selection_Of_Tables
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

class HomeMenu_Activity : AppCompatActivity() {

    val context: Context = this
    private lateinit var binding: ActivityHomeMenuBinding
    private lateinit var auth: FirebaseAuth
    lateinit var daoTiposFood: DaoFoodType
    lateinit var daoTiposGame: DaoGameType



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityHomeMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        val dataBase = Room.databaseBuilder(
            context,
            GeneralDataBase::class.java,
            DATABASE_NAME
        )
//            .fallbackToDestructiveMigration()
            .build()
        daoTiposFood = dataBase.DaoFoodType()
        daoTiposGame = dataBase.DaoGameType()


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        insertTipos()

        irBack()
        setupListeners()
    }

    fun insertTipos() {
        lifecycleScope.launch {
            if (daoTiposFood.getAll().size == 0) {
                val listaf = listOf<FoodTypeDataClass>(
                    FoodTypeDataClass(1, "Comida"),
                    FoodTypeDataClass(2, "Bebida"),
                    FoodTypeDataClass(3, "Postre"),
                    FoodTypeDataClass(4, "Caliente"),
                    FoodTypeDataClass(5, "Frio"),
                    FoodTypeDataClass(6, "Frito"),
                    FoodTypeDataClass(7, "Congelado")
                )
                daoTiposFood.insertAll(listaf)
            }
        }
        lifecycleScope.launch {
            if (daoTiposGame.getAll().size == 0) {
                val listag = listOf<GameTypeDataClass>(
                    GameTypeDataClass(1, "Cartas"),
                    GameTypeDataClass(2, "Tablero"),
                    GameTypeDataClass(3, "Fantasia"),
                    GameTypeDataClass(4, "Estrategia"),
                    GameTypeDataClass(5, "Corto"),
                    GameTypeDataClass(6, "Medio"),
                    GameTypeDataClass(7, "Largo"),
                    GameTypeDataClass(8, "Cooperativo"),
                    GameTypeDataClass(9, "Competencia")
                )
                daoTiposGame.insertAll(listag)
            }
        }

    }


    fun irBack() {
        binding.buttonBackButton.setOnClickListener {
            auth.signOut()
            onBackPressedDispatcher.onBackPressed()
        }

    }

     fun setupListeners() {
        binding.cardViewFoodMenu.setOnClickListener {
            val intent = Intent(this, FoodMenuActivity::class.java)
            startActivity(intent)
        }

        binding.cardViewGamesMenu.setOnClickListener {
            val intent = Intent(this, GamesMenuActivity::class.java)
            startActivity(intent)
        }

        binding.cardViewTablesMenu.setOnClickListener {
            val intent = Intent(this, Selection_Of_Tables::class.java)
            startActivity(intent)
        }
    }

}