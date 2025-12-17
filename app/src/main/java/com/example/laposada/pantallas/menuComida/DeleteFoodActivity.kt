package com.example.laposada.pantallas.menuComida

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.laposada.R
import com.example.laposada.adapters.FoodSpinnerAdapter
import com.example.laposada.dataBase.DaoFood
import com.example.laposada.dataBase.GeneralDataBase
import com.example.laposada.dataClass.FoodDataClass
import com.example.laposada.databinding.ActivityDeleteFoodBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DeleteFoodActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDeleteFoodBinding
    private val context = this
    lateinit var daoFood: DaoFood
    private val foodList: MutableList<FoodDataClass> = mutableListOf()

    private val spinnerFoodAdapter: FoodSpinnerAdapter by lazy {
        FoodSpinnerAdapter(this, foodList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDeleteFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val database: GeneralDataBase = Room.databaseBuilder(
            context, GeneralDataBase::class.java,
            FoodMenuActivity.Companion.DATABASE_NAME
        )
//            .fallbackToDestructiveMigration()
            .build()
        daoFood = database.DaoFood()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.spinnerComida.adapter = spinnerFoodAdapter
        loadFoods()
        setupListeners()

    }

     fun setupListeners() {
        binding.buttonBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.buttonDelete.setOnClickListener {
            deleteSelectedFood()
        }
    }
    private fun loadFoods() {
        lifecycleScope.launch {
            val foods = withContext(Dispatchers.IO) {
                daoFood.getAll()
            }
            foodList.clear()
            foodList.addAll(foods)
            if (foodList.isEmpty()) {
                Toast.makeText(
                    this@DeleteFoodActivity,
                    "No hay comidas",
                    Toast.LENGTH_SHORT
                ).show()
            }
            spinnerFoodAdapter.notifyDataSetChanged()
        }
    }

    private fun deleteSelectedFood() {
        val selectedFood = binding.spinnerComida.selectedItem as FoodDataClass

        lifecycleScope.launch(Dispatchers.IO) {
            daoFood.deleteById(selectedFood.id)

            withContext(Dispatchers.Main) {
                Toast.makeText(
                    this@DeleteFoodActivity,
                    "Comida eliminada",
                    Toast.LENGTH_SHORT
                ).show()
                loadFoods()
            }
        }
    }
}