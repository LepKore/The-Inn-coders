package com.example.laposada.pantallas.menuComida

import android.app.AlertDialog
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
import com.example.laposada.R
import com.example.laposada.dataBase.DaoFood
import com.example.laposada.dataBase.DaoFoodType
import com.example.laposada.dataBase.GeneralDataBase
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
            uri?.let {
                val imageId = saveImageToInternalStorage(it)
                currentImageId = imageId
                val file = File(filesDir, imageId)
                binding.imagePlaceHolder.background = null
                binding.imagePlaceHolder.setBackgroundDrawable(
                    android.graphics.drawable.Drawable.createFromPath(file.absolutePath)
                )
            }
        }

    private lateinit var tipos: List<FoodTypeDataClass>
    private val selectedTipos = mutableListOf<Int>()

    private val context = this
    lateinit var daoTipos: DaoFoodType
    lateinit var daoFood: DaoFood


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dataBase = Room.databaseBuilder(
            context,
            GeneralDataBase::class.java,
            FoodMenuActivity.Companion.DATABASE_NAME
        )
//            .fallbackToDestructiveMigration()
            .build()
        daoTipos    = dataBase.DaoFoodType()
        daoFood     = dataBase.DaoFood()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        loadTipos()
        setupListeners()
    }

     fun setupListeners() {
         binding.buttonBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
         }

         binding.Type.setOnClickListener {
             val names = tipos.map { it.nombre }.toTypedArray()
             val checkedItems = BooleanArray(tipos.size) { index ->
                 selectedTipos.contains(tipos[index].id)
             }

             androidx.appcompat.app.AlertDialog.Builder(context)
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
            onBackPressedDispatcher.onBackPressed()
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
                daoTipos.getAll()
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