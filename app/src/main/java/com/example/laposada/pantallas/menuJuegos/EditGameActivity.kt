package com.example.laposada.pantallas.menuJuegos

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
import com.example.laposada.adapters.GameSpinnerAdapter
import com.example.laposada.dataBase.DaoGame
import com.example.laposada.dataBase.DaoGameType
import com.example.laposada.dataBase.GeneralDataBase
import com.example.laposada.dataClass.GameDataClass
import com.example.laposada.dataClass.GameTypeDataClass
import com.example.laposada.databinding.ActivityEditGameBinding
import com.example.laposada.pantallas.menuComida.FoodMenuActivity.Companion.DATABASE_NAME
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class EditGameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditGameBinding
    private val context = this

    private lateinit var daoGame: DaoGame
    private lateinit var daoTipos: DaoGameType

    private val gameList = mutableListOf<GameDataClass>()
    private val spinnerAdapter by lazy {
        GameSpinnerAdapter(this, gameList)
    }

    private var currentImageCajaId: String? = null
    private var currentImageGameId: String? = null
    private var selectedGame: GameDataClass? = null

    private lateinit var tipos: List<GameTypeDataClass>
    private val selectedTipos = mutableListOf<Int>()

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEditGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = Room.databaseBuilder(
            context,
            GeneralDataBase::class.java,
            DATABASE_NAME
        ).build()

        daoGame = database.DaoGame()
        daoTipos = database.DaoGameType()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.spinnerJuego.adapter = spinnerAdapter

        loadGames()
        loadTipos()
        setupListeners()
    }


    private fun setupListeners() {
        binding.buttonBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.spinnerJuego.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: android.view.View?,
                    position: Int,
                    id: Long
                ) {
                    selectedGame = gameList[position]
                    fillFields(selectedGame!!)
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

        binding.imagePlaceHolder1.setOnClickListener {
            pickImageLauncherCaja.launch("image/*")
        }
        binding.imagePlaceHolder2.setOnClickListener {
            pickImageLauncherGame.launch("image/*")
        }

        binding.buttonSave.setOnClickListener {
            updateGame()
        }

    }

    private fun fillFields(game: GameDataClass) {
        binding.inputName.setText(game.nombre)
        binding.inputDescription.setText(game.descripcion)

        selectedTipos.clear()
        selectedTipos.addAll(game.categorias)
        lifecycleScope.launch {
            val nombresTipos = withContext(Dispatchers.IO) {
                selectedTipos.mapNotNull { id -> daoTipos.selectById(id) }
            }
            binding.Type.text = if (nombresTipos.isEmpty()) "Seleccione los tipos"
            else nombresTipos.joinToString(", ")
        }

        currentImageCajaId = game.imagenCaja
        currentImageGameId = game.imagenGame
        val fileCaja = File(filesDir, game.imagenCaja)
        val fileGame = File(filesDir, game.imagenGame)

        if (fileCaja.exists()) {
            binding.imagePlaceHolder1.background = null
            binding.imagePlaceHolder1.setBackgroundDrawable(
                android.graphics.drawable.Drawable.createFromPath(fileCaja.absolutePath)
            )
        }
        if (fileGame.exists()) {
            binding.imagePlaceHolder2.background = null
            binding.imagePlaceHolder2.setBackgroundDrawable(
                android.graphics.drawable.Drawable.createFromPath(fileGame.absolutePath)
            )
        }
    }
    private fun loadGames() {
        lifecycleScope.launch {
            val games = withContext(Dispatchers.IO) {
                daoGame.getAll()
            }
            gameList.clear()
            gameList.addAll(games)
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
    private fun updateGame() {
        val game = selectedGame ?: return

        val updated = game.copy(
            nombre = binding.inputName.text.toString(),
            descripcion = binding.inputDescription.text.toString(),
            imagenCaja = currentImageCajaId!!,
            imagenGame = currentImageGameId!!,
            categorias = selectedTipos
        )

        lifecycleScope.launch(Dispatchers.IO) {
            daoGame.update(updated)
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Juego actualizado", Toast.LENGTH_SHORT).show()
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