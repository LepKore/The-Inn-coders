package com.example.laposada

import android.R.attr.data
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.laposada.FoodMenuActivity.Companion.FOOD_DATABASE_NAME
import com.example.laposada.adapters.LabelAdapter
import com.example.laposada.dataBase.DaoFood
import com.example.laposada.dataBase.FoodDatabase
import com.example.laposada.dataClass.FoodDataClass
import com.example.laposada.databinding.ActivityFoodDescriptionBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class FoodDescriptionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFoodDescriptionBinding
    private val context = this
    private lateinit var daoFood: DaoFood
    private val adapter: LabelAdapter by lazy { LabelAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityFoodDescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val foodDatabase = Room.databaseBuilder(
            context, FoodDatabase::class.java, FOOD_DATABASE_NAME
        )
        daoFood = foodDatabase.build().DaoFood()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        lifecycleScope.launch {
            var foodList = withContext(Dispatchers.IO) {
                obtenerDatos()
            }
            val foodId = intent.getIntExtra(FoodMenuActivity.FOOD_ID, -1)
            val food = foodList.find { it.id == foodId }
            food?.let { f ->
                val imageFile = File(binding.root.context.filesDir, f.imagen)
                binding.layoutImageFood.background =
                    Drawable.createFromPath(imageFile.path)
                binding.textViewFoodName.text = f.nombre
                binding.textViewPrice.text = "Bs. " + f.precio.toString()
                binding.textViewDescriptionFood.text = f.descripcion
                adapter.addDataCards(f.tipos)
            }
            setupRecyclerView()
            setupListeners()
        }

    }

    private suspend fun obtenerDatos(): List<FoodDataClass> {
        return daoFood.getAll()
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