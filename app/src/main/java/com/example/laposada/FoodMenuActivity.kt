package com.example.laposada

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.room.Room
import com.example.laposada.adapters.FoodAdapter
import com.example.laposada.dataBase.DaoFood
import com.example.laposada.dataBase.FoodDatabase
import com.example.laposada.dataClass.FoodDataClass
import com.example.laposada.databinding.ActivityFoodMenuBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class FoodMenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFoodMenuBinding
    private lateinit var sharedPreferences: SharedPreferences
    private val context = this
    private lateinit var daoFood: DaoFood
    private val adapter: FoodAdapter by lazy { FoodAdapter {food ->
        openFoodDetail(food)
    } }

    companion object {
        val TAG_SHARED_PREFERENCES = "TAG_SHARED_PREFERENCES"
        val FOOD_DATABASE_NAME = "FOOD_DATABASE_NAME"
        val FOOD_ID = "FOOD_ID"
    }

    private val launcherEditMenu = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            lifecycleScope.launch {
                val food = withContext(Dispatchers.IO) {
                    obtenerDatosEnBaseDeDatos()
                }
                adapter.addDataCards(food)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFoodMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = context.getSharedPreferences(
            TAG_SHARED_PREFERENCES,MODE_PRIVATE
        )
        val foodDatabase = Room.databaseBuilder(
            context, FoodDatabase::class.java, FOOD_DATABASE_NAME
        )
        daoFood = foodDatabase.build().DaoFood()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupRecyclerView()
        setupListeners()

        lifecycleScope.launch {
            val food = withContext(Dispatchers.IO) { obtenerDatosEnBaseDeDatos() }
            adapter.addDataCards(food)
        }

    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            val food = withContext(Dispatchers.IO) { obtenerDatosEnBaseDeDatos() }
            adapter.addDataCards(food)
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerViewFood.layoutManager = GridLayoutManager(this, 2)
        binding.recyclerViewFood.adapter = adapter
    }

    private fun setupListeners() {
        binding.buttonBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.buttonEdit.setOnClickListener {
            val editFoodIntent = Intent(context, EditMenuOptionsActivity::class.java)
            launcherEditMenu.launch(editFoodIntent)
        }

    }
    private fun openFoodDetail(food: FoodDataClass) {
        val intent = Intent(context, FoodDescriptionActivity::class.java)
        intent.putExtra(FOOD_ID, food.id)  // solo pasamos el ID por ahora
        startActivity(intent)
    }

    private suspend fun guardarDatosBD() {
        val foodList = listOf(
            FoodDataClass(
                id = 0,
                nombre = "Papas fritas",
                precio = 10.0,
                imagen = R.drawable.papas_fritas,
                descripcion = "Papas fritas con sal",
                tipos = listOf("comida", "aperitivo")
            ),
            FoodDataClass(
                id = 0,
                nombre = "Hamburguesa",
                precio = 12.0,
                imagen = R.drawable.hamburguesaa,
                descripcion = "Hamburguesa con queso",
                tipos = listOf("comida", "aperitivo")
            ),
            FoodDataClass(
                id = 0,
                nombre = "Pizza",
                precio = 15.0,
                imagen = R.drawable.pizza,
                descripcion = "Pizza con tomate",
                tipos = listOf("comida", "aperitivo")
            )
        )

        daoFood.insertAll(foodList)
    }

    private suspend fun obtenerDatosEnBaseDeDatos():List<FoodDataClass>  {
        return daoFood.getAll()
    }


}