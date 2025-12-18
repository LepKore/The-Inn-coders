package com.example.laposada.pantallas.startMenu

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
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
import com.example.laposada.dataBase.DaoFood
import com.example.laposada.dataBase.DaoFoodType
import com.example.laposada.dataBase.DaoGame
import com.example.laposada.dataBase.DaoGameType
import com.example.laposada.dataBase.GeneralDataBase
import com.example.laposada.dataClass.FoodDataClass
import com.example.laposada.dataClass.FoodTypeDataClass
import com.example.laposada.dataClass.GameDataClass
import com.example.laposada.dataClass.GameTypeDataClass
import com.example.laposada.databinding.ActivityHomeMenuBinding
import com.example.laposada.pantallas.menuComida.FoodMenuActivity.Companion.DATABASE_NAME
import com.example.laposada.pantallas.mesas.Selection_Of_Tables
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class HomeMenu_Activity : AppCompatActivity() {

    val context: Context = this
    private lateinit var binding: ActivityHomeMenuBinding
    private lateinit var auth: FirebaseAuth
    lateinit var daoTiposFood: DaoFoodType
    lateinit var daoTiposGame: DaoGameType
    lateinit var daoFood: DaoFood
    lateinit var daoGame: DaoGame





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
        daoGame = dataBase.DaoGame()
        daoFood = dataBase.DaoFood()


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        insertTipos()
        irBack()
        setupListeners()
    }

    fun deleteEverything() {
        lifecycleScope.launch {
            daoTiposFood.deleteAll()
            daoTiposGame.deleteAll()
            daoGame.deleteAll()
            daoFood.deleteAll()
        }
    }

    fun insertTipos() {
        lifecycleScope.launch {
            if (daoTiposFood.getAll().isEmpty()) {
                val tiposComida = listOf(
                    FoodTypeDataClass(1, "Comida"),
                    FoodTypeDataClass(2, "Bebida"),
                    FoodTypeDataClass(3, "Postre"),
                    FoodTypeDataClass(4, "Caliente"),
                    FoodTypeDataClass(5, "Frio"),
                    FoodTypeDataClass(6, "Frito"),
                    FoodTypeDataClass(7, "Congelado")
                )
                daoTiposFood.insertAll(tiposComida)
            }
            if (daoFood.getAll().isEmpty()) {
                val imageName1 = copyDrawableToInternalStorage(context, R.drawable.coca_cola, "coca_cola.png")
                val imageName2 = copyDrawableToInternalStorage(context, R.drawable.hamburguesaa, "hamburguesa.png")
                val imageName3 = copyDrawableToInternalStorage(context, R.drawable.papas_fritas, "papas_fritas.png")
                val imageName4 = copyDrawableToInternalStorage(context, R.drawable.pizza, "pizza.png")
                val imageName5 = copyDrawableToInternalStorage(context, R.drawable.sandwich, "sandwich.png")

                val comidas = listOf(
                    FoodDataClass(1, "Coca Cola", 8.0, imageName1, "Un vaso grande de Coca Cola", listOf(2,5)),
                    FoodDataClass(2, "Hamburguesa", 15.0, imageName2, "Una hamburguesa completa", listOf(1,4,6)),
                    FoodDataClass(3, "Papas fritas", 15.0, imageName3, "Papas fritas con aderezos", listOf(1,4,6)),
                    FoodDataClass(4, "Pizza", 20.0, imageName4, "Pizza con muzzarella", listOf(1,4)),
                    FoodDataClass(5, "Sandwich", 10.0, imageName5, "Sandwich con tomate", listOf(1,4))
                )

                daoFood.insertAll(comidas)
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
            if (daoGame.getAll().isEmpty()) {
                val sushi1 = copyDrawableToInternalStorage(context, R.drawable.sushigo1, "sushigo1.png")
                val sushi2 = copyDrawableToInternalStorage(context, R.drawable.sushigo2, "sushigo2.png")
                val sabo1 = copyDrawableToInternalStorage(context, R.drawable.saboteur1, "saboteur1.png")
                val sabo2 = copyDrawableToInternalStorage(context, R.drawable.saboteur2, "saboteur2.png")
                val tanxi1 = copyDrawableToInternalStorage(context, R.drawable.tanxi1, "tanxi1.png")
                val tanxi2 = copyDrawableToInternalStorage(context, R.drawable.tanxi2, "tanxi2.png")
//                val coffee1 = copyDrawableToInternalStorage(context, R.drawable.coffer1, "coffee1.png")
//                val coffee2 = copyDrawableToInternalStorage(context, R.drawable.coffer2, "coffee2.png")
                val soviet1 = copyDrawableToInternalStorage(context, R.drawable.soviet1, "soviet1.png")
                val soviet2 = copyDrawableToInternalStorage(context, R.drawable.soviet2, "soviet2.png")


                val games = listOf(
                    GameDataClass(1, "Sushi Go", sushi2, sushi1,
                        "Un juego muy divertido donde tienes que armar tu jugada se sushi. Obten puntos y ganales al resto con tu combinacion.",
                        listOf(1,5,9)),
                    GameDataClass(2, "Saboteur", sabo2, sabo1,
                        "Tu y tus amigos estan en busca del oro en una mina, cooperen para llegar a su objetivo. Pero no se confien, parece que alguien intenta saborearlos."
                        ,listOf(1, 6, 8)),
                    GameDataClass(3, "Tanxi", tanxi2, tanxi1,
                        "Embarcaste en una isla, con el objetivo de ser el ganador. Armate, investiga y quizas pide algunas ayudas a los dioses y tendras una oportunidad."
                        ,listOf(2, 3, 7, 9)),
//                    GameDataClass(4, "Coffee Rush", coffee2, coffee1,
//                        "Invita a tus amigos para ver quien puede ser el mejor dueno de una cafeteria. Prepara los pedidos, y cuidado con que salgan demasiado tarde"
//                        , listOf(2, 6, 9)),
                    GameDataClass(4, "Soviet Kitchen", soviet2, soviet1,
                        "Se creen capaces de hacer cocina sovietica. Bueno, este juego los pondra a prueba en este interesante reto"
                        , listOf(1, 7, 8))
                )
                daoGame.insertAll(games)
            }
        }

    }

    fun copyDrawableToInternalStorage(
        context: Context,
        drawableResId: Int,
        fileName: String
    ): String {
        val file = File(context.filesDir, fileName)
        if (file.exists()) return fileName

        val bitmap = BitmapFactory.decodeResource(context.resources, drawableResId)
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }

        return fileName
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