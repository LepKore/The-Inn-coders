package com.example.laposada.pantallas.startMenu

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.laposada.R
import com.example.laposada.databinding.ActivityLoginBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class Login_activity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    val context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val currentUser = auth.currentUser

        if (currentUser != null) {
            val intentUsuarioLogueado = Intent(context, HomeMenu_Activity::class.java)
            startActivity(intentUsuarioLogueado)
        }

        irStart()
        irHome()


    }

    fun irStart() {
        binding.buttonBackButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    fun irHome() {
        binding.buttonSend.setOnClickListener {
            val correo = binding.editTextLoginMail.text.toString()
            val password = binding.editTextPasswordUser.text.toString()
            loginUsuario(correo, password)
        }
    }

    fun loginUsuario(correo: String, password: String) {

         auth.signInWithEmailAndPassword(correo, password)
             .addOnCompleteListener { task ->
                 if (task.isSuccessful) {
                     // Nuestro usuario se logueo correctamente
                     val intent = Intent(context, HomeMenu_Activity::class.java)
                     startActivity(intent)

                 } else {
                     // El usuario no se pudo loguear
                     Toast.makeText(
                         baseContext,
                         "No se pudo iniciar sesion",
                         Toast.LENGTH_LONG,
                     ).show()
                 }
             }

    }


}