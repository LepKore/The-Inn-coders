package com.example.laposada.pantallas.menuComida

import android.content.Intent
import android.content.SharedPreferences
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
import com.example.laposada.adapters.FoodAdapter
import com.example.laposada.dataBase.DaoFood
import com.example.laposada.dataBase.GeneralDataBase
import com.example.laposada.dataClass.FoodDataClass
import com.example.laposada.databinding.ActivityFoodMenuBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FoodMenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFoodMenuBinding
    private lateinit var sharedPreferences: SharedPreferences
    private val context = this
    private lateinit var daoFood: DaoFood
    private val adapter: FoodAdapter by lazy {
        FoodAdapter { food ->
            openFoodDetail(food)
        }
    }

    companion object {
        val TAG_SHARED_PREFERENCES = "TAG_SHARED_PREFERENCES"
        val DATABASE_NAME = "DATABASE_NAME"
        val FOOD_ID = "FOOD_ID"
    }

    private val launcherEditMenu = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            lifecycleScope.launch {
                val food = withContext(Dispatchers.IO) {
                    getFoodList()
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

        val dataBase = Room.databaseBuilder(
            context,
            GeneralDataBase::class.java,
            DATABASE_NAME
        )
//            .fallbackToDestructiveMigration()
            .build()
        daoFood = dataBase.DaoFood()

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
        drawFood()
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
        intent.putExtra(FOOD_ID, food.id)
        startActivity(intent)
    }

    private suspend fun getFoodList():List<FoodDataClass>  {
        val food = withContext(Dispatchers.IO) { daoFood.getAll() }
        return food
    }

    private fun drawFood() {
        lifecycleScope.launch {
            val food = getFoodList()
            adapter.addDataCards(food)
        }
    }


}