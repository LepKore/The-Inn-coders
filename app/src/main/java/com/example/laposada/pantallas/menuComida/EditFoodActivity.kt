package com.example.laposada.pantallas.menuComida

import android.net.Uri
import android.os.Bundle
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.laposada.R
import com.example.laposada.adapters.FoodSpinnerAdapter
import com.example.laposada.dataBase.DaoFood
import com.example.laposada.dataBase.DaoFoodType
import com.example.laposada.dataBase.GeneralDataBase
import com.example.laposada.dataClass.FoodDataClass
import com.example.laposada.dataClass.FoodTypeDataClass
import com.example.laposada.databinding.ActivityEditFoodBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class EditFoodActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditFoodBinding
    private val context = this

    // DB
    private lateinit var daoFood: DaoFood
    private lateinit var daoTipos: DaoFoodType

    // Spinner
    private val foodList = mutableListOf<FoodDataClass>()
    private val spinnerAdapter by lazy {
        FoodSpinnerAdapter(this, foodList)
    }

    // Tipos
    private lateinit var tipos: List<FoodTypeDataClass>
    private val selectedTipos = mutableListOf<Int>()

    // Imagen
    private var currentImageId: String? = null
    private var selectedFood: FoodDataClass? = null

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                val imageId = saveImageToInternalStorage(it)
                currentImageId = imageId
                val file = File(filesDir, imageId)
                binding.imagePlaceHolder2.background = null
                binding.imagePlaceHolder2.setBackgroundDrawable(
                    android.graphics.drawable.Drawable.createFromPath(file.absolutePath)
                )
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEditFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = Room.databaseBuilder(
            context,
            GeneralDataBase::class.java,
            FoodMenuActivity.Companion.DATABASE_NAME
        ).build()

        daoFood = database.DaoFood()
        daoTipos = database.DaoFoodType()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.spinnerComida.adapter = spinnerAdapter

        loadFoods()
        loadTipos()
        setupListeners()
    }

    private fun setupListeners() {

        binding.buttonBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.spinnerComida.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: android.view.View?,
                    position: Int,
                    id: Long
                ) {
                    selectedFood = foodList[position]
                    fillFields(selectedFood!!)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }

        binding.Type.setOnClickListener {
            val names = tipos.map { it.nombre }.toTypedArray()
            val checkedItems = BooleanArray(tipos.size) { index ->
                selectedTipos.contains(tipos[index].id)
            }

            AlertDialog.Builder(context)
                .setTitle("Seleccione los tipos")
                .setMultiChoiceItems(names, checkedItems) { _, which, isChecked ->
                    val tipoId = tipos[which].id
                    if (isChecked) selectedTipos.add(tipoId)
                    else selectedTipos.remove(tipoId)
                }
                .setPositiveButton("Aceptar") { dialog, _ ->
                    lifecycleScope.launch {
                        val nombresTipos = withContext(Dispatchers.IO) {
                            selectedTipos.mapNotNull { id -> daoTipos.selectById(id) }
                        }
                        binding.Type.text = if (nombresTipos.isEmpty()) "Seleccione los tipos"
                        else nombresTipos.joinToString(", ")
                        dialog.dismiss()
                    }
                }
                .setNegativeButton("Cancelar") { dialog, _ -> dialog.dismiss() }
                .show()
        }


        binding.imagePlaceHolder2.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        binding.buttonEdit.setOnClickListener {
            updateFood()
        }

        binding.inputDescripcion.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                binding.scrollView.post {
                    binding.scrollView.smoothScrollTo(0, v.bottom)
                }
            }
        }
    }

    private fun fillFields(food: FoodDataClass) {
        binding.inputName2.setText(food.nombre)
        binding.inputPrice2.setText(food.precio.toString())
        binding.inputDescripcion.setText(food.descripcion)

        selectedTipos.clear()
        selectedTipos.addAll(food.tipos)
        lifecycleScope.launch {
            val nombresTipos = withContext(Dispatchers.IO) {
                selectedTipos.mapNotNull { id -> daoTipos.selectById(id) }
            }
            binding.Type.text = if (nombresTipos.isEmpty()) "Seleccione los tipos"
            else nombresTipos.joinToString(", ")
        }

        currentImageId = food.imagen
        val file = File(filesDir, food.imagen)
        if (file.exists()) {
            binding.imagePlaceHolder2.background = null
            binding.imagePlaceHolder2.setBackgroundDrawable(
                android.graphics.drawable.Drawable.createFromPath(file.absolutePath)
            )
        }
    }

    private fun loadFoods() {
        lifecycleScope.launch {
            val foods = withContext(Dispatchers.IO) {
                daoFood.getAll()
            }
            foodList.clear()
            foodList.addAll(foods)
            spinnerAdapter.notifyDataSetChanged()
        }
    }

    private fun loadTipos() {
        lifecycleScope.launch {
            tipos = withContext(Dispatchers.IO) {
                daoTipos.getAll()
            }
        }
    }

    private fun updateFood() {
        val food = selectedFood ?: return

        val updated = food.copy(
            nombre = binding.inputName2.text.toString(),
            precio = binding.inputPrice2.text.toString().toDouble(),
            descripcion = binding.inputDescripcion.text.toString(),
            imagen = currentImageId!!,
            tipos = selectedTipos
        )

        lifecycleScope.launch(Dispatchers.IO) {
            daoFood.update(updated)
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Comida actualizada", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveImageToInternalStorage(uri: Uri): String {
        val fileName = "food_${System.currentTimeMillis()}.jpg"
        val file = File(filesDir, fileName)
        contentResolver.openInputStream(uri)?.use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }
        return fileName
    }
}