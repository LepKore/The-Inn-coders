package com.example.laposada.pantallas.menuJuegos

import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.laposada.R
import com.example.laposada.dataBase.DaoGame
import com.example.laposada.dataBase.DaoGameType
import com.example.laposada.dataBase.GeneralDataBase
import com.example.laposada.dataClass.FoodDataClass
import com.example.laposada.dataClass.GameDataClass
import com.example.laposada.dataClass.GameTypeDataClass
import com.example.laposada.databinding.ActivityAddGameBinding
import com.example.laposada.pantallas.menuComida.FoodMenuActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class AddGameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddGameBinding
    private val context = this

    private var currentImageCajaId: String? = null
    private var currentImageGameId: String? = null

    private val pickImageLauncherCaja =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                val imageId = saveImageToInternalStorage(it)
                currentImageCajaId = imageId
                val file = File(filesDir, imageId)
                binding.imagePlaceHolder1.background = null
                binding.imagePlaceHolder1.setBackgroundDrawable(
                    android.graphics.drawable.Drawable.createFromPath(file.absolutePath)
                )
            }
        }
    private val pickImageLauncherGame =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                val imageId = saveImageToInternalStorage(it)
                currentImageGameId = imageId
                val file = File(filesDir, imageId)
                binding.imagePlaceHolder2.background = null
                binding.imagePlaceHolder2.setBackgroundDrawable(
                    android.graphics.drawable.Drawable.createFromPath(file.absolutePath)
                )
            }
        }

    private lateinit var tipos: List<GameTypeDataClass>
    private val selectedTipos = mutableListOf<Int>()
    lateinit var daoTipos: DaoGameType
    lateinit var daoGame: DaoGame


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dataBase = Room.databaseBuilder(
            context,
            GeneralDataBase::class.java,
            FoodMenuActivity.Companion.DATABASE_NAME
        )
//            .fallbackToDestructiveMigration()
            .build()
        daoTipos    = dataBase.DaoGameType()
        daoGame     = dataBase.DaoGame()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        loadTipos()
        setupListeners()
    }

    private fun setupListeners() {
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

        binding.imagePlaceHolder1.setOnClickListener {
            pickImageLauncherCaja.launch("image/*")
        }
        binding.imagePlaceHolder2.setOnClickListener {
            pickImageLauncherGame.launch("image/*")
        }

        binding.buttonAdd.setOnClickListener {
            val nombre = binding.inputName.text.toString()
            val imagenCaja = currentImageCajaId
            val imagenGame = currentImageGameId
            val descripcion = binding.inputDescription.text.toString()
            val tipos = selectedTipos

            if (nombre.isEmpty() ||
                imagenCaja.isNullOrEmpty() ||
                imagenGame.isNullOrEmpty() ||
                descripcion.isEmpty() ||
                tipos.isEmpty()
            ) {
                Toast.makeText(context, "Rellena todos los campos e imagen", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val game = GameDataClass(0, nombre, imagenCaja, imagenGame, descripcion, tipos)
            addGame(game)
            Toast.makeText(context, "Juego añadido", Toast.LENGTH_SHORT).show()
            onBackPressedDispatcher.onBackPressed()
        }
    }
    private fun saveImageToInternalStorage(uri: Uri): String {
        val inputStream = contentResolver.openInputStream(uri)
        val fileName = "game_${System.currentTimeMillis()}.jpg"
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
    private fun addGame(gameDataClass: GameDataClass) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                daoGame.insert(gameDataClass)
            }
        }
    }

}