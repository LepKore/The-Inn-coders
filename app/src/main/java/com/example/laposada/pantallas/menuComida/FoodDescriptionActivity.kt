package com.example.laposada.pantallas.menuComida

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
import com.example.laposada.dataBase.DaoFood
import com.example.laposada.dataBase.DaoFoodType
import com.example.laposada.dataBase.GeneralDataBase
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
    private lateinit var daoFoodType: DaoFoodType
    private val adapter: LabelAdapter by lazy { LabelAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityFoodDescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dataBase = Room.databaseBuilder(
            context,
            GeneralDataBase::class.java,
            FoodMenuActivity.Companion.DATABASE_NAME
        )
//            .fallbackToDestructiveMigration()
            .build()

        daoFood     = dataBase.DaoFood()
        daoFoodType = dataBase.DaoFoodType()


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        lifecycleScope.launch {

            val foodId = intent.getIntExtra(FoodMenuActivity.FOOD_ID, -1)
            val result = withContext(Dispatchers.IO) {
                val food = daoFood.getAll().find { it.id == foodId }

                val tipos = food?.tipos?.map { id ->
                    daoFoodType.selectById(id)
                }

                Pair(food, tipos)
            }

            val food = result.first
            val tipos = result.second
            food?.let { f ->
                val imageFile = File(filesDir, f.imagen)
                if (imageFile.exists()) {
                    binding.layoutImageFood.background =
                        Drawable.createFromPath(imageFile.path)
                }

                binding.textViewFoodName.text = f.nombre
                binding.textViewPrice.text = "Bs. ${f.precio}"
                binding.textViewDescriptionFood.text = f.descripcion

                tipos?.let {
                    adapter.addDataCards(it)
                }
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