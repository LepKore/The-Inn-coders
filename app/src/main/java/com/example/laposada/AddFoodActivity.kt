package com.example.laposada

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.laposada.FoodMenuActivity.Companion.FOOD_DATABASE_NAME
import com.example.laposada.dataBase.DaoFood
import com.example.laposada.dataBase.DaoFoodType
import com.example.laposada.dataBase.FoodDatabase
import com.example.laposada.dataBase.FoodTypeDataBase
import com.example.laposada.dataClass.FoodDataClass
import com.example.laposada.dataClass.FoodTypeDataClass
import com.example.laposada.databinding.ActivityAddFoodBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class AddFoodActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddFoodBinding

    private var currentImageId: String? = null

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                val imageId = saveImageToInternalStorage(uri)
                currentImageId = imageId
                val file = File(filesDir, imageId)
                binding.imagePlaceHolder.setImageURI(file.toUri())
            }
        }

    private lateinit var tipos: List<String>
    private val selectedTipos = mutableListOf<String>()

    private val context = this
    lateinit var daoTipos: DaoFoodType
    lateinit var daoFood: DaoFood

    companion object {
        val FOOD_TYPE_DATABASE_NAME = "FOOD_TYPE_DATABASE_NAME"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val typeDB = Room.databaseBuilder(
            context,
            FoodTypeDataBase::class.java,
            FOOD_TYPE_DATABASE_NAME
        ).build()
        daoTipos = typeDB.DaoFoodType()

        val foodDB = Room.databaseBuilder(
            context,
            FoodDatabase::class.java,
            FOOD_DATABASE_NAME
        ).build()
        daoFood = foodDB.DaoFood()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        insertTipos()

        loadTipos()
        setupListeners()
    }
    fun insertTipos() {
        val lista = listOf<FoodTypeDataClass>(
            FoodTypeDataClass(0, "Comida"),
            FoodTypeDataClass(0, "Bebida"),
            FoodTypeDataClass(0, "Postre"),
            FoodTypeDataClass(0, "Caliente"),
            FoodTypeDataClass(0, "Frio"),
            FoodTypeDataClass(0, "Frito"),
            FoodTypeDataClass(0, "Congelado")
        )
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                daoTipos.deleteAll()
//                daoTipos.insertAll(lista)
            }

        }
    }

     fun setupListeners() {
         binding.buttonBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
         }

         binding.Type.setOnClickListener {
             val selectedItems = BooleanArray(tipos.size) {index ->
                 selectedTipos.contains(tipos[index])

             }

             val builder = android.app.AlertDialog.Builder(context)
             builder.setTitle("Selecciones los tipos")
             builder.setMultiChoiceItems(tipos.toTypedArray(), selectedItems) {
                 _, which, isChecked ->
                if (isChecked) {
                    selectedTipos.add(tipos[which])
                } else {
                    selectedTipos.remove(tipos[which])

                }
             }
             builder.setPositiveButton("Aceptar") {dialog, _ ->
                 binding.Type.text = if (selectedTipos.isEmpty()) "Seleccione los tipos" else selectedTipos.joinToString(", ")
                 dialog.dismiss()
             }
             builder.setNegativeButton("Cancelar") { dialog, _ ->
                 dialog.dismiss()
             }
             builder.create().show()
         }

         binding.imagePlaceHolder.setOnClickListener {
             pickImageLauncher.launch("image/*")
         }


        binding.buttonAdd.setOnClickListener {
            val nombre = binding.inputName.text.toString()
            var precio = binding.inputPrice.text.toString()
            val imagen = currentImageId
            val descripcion = binding.inputDescripcion.text.toString()
            val tipos = selectedTipos

            if (nombre.isEmpty() ||
                precio.isEmpty() ||
                imagen.isNullOrEmpty() ||
                descripcion.isEmpty() ||
                tipos.isEmpty()
                ) {
                Toast.makeText(context, "Rellena todos los campos e imagen", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val food = FoodDataClass(0, nombre, precio.toDouble(), imagen, descripcion, tipos)
            addFood(food)
            Toast.makeText(context, "Comida añadida", Toast.LENGTH_SHORT).show()

        }
    }

    private fun saveImageToInternalStorage(uri: Uri): String {
        val inputStream = contentResolver.openInputStream(uri)
        val fileName = "food_${System.currentTimeMillis()}.jpg"
        val file = File(filesDir, fileName)

        inputStream.use { input ->
            FileOutputStream(file).use { output ->
                input?.copyTo(output)
            }
        }

        return fileName
    }

    private fun loadTipos() {
        lifecycleScope.launch {
            tipos = withContext(Dispatchers.IO) {
                daoTipos.getNombres()
            }
        }
    }

    private fun addFood(food: FoodDataClass) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                daoFood.insert(food)
            }
        }
    }


}