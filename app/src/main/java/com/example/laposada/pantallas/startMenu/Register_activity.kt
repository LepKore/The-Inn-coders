package com.example.laposada.pantallas.startMenu

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.laposada.R
import com.example.laposada.databinding.ActivityRegisterBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class Register_activity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    val context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        irStart()
        confirmarPassword()
    }

    fun irStart() {
        binding.buttonBackButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    fun confirmarPassword() {
        binding.buttonSend.setOnClickListener {
            val mail = binding.editTextLoginMail.text.toString().trim()
            val password = binding.editTextPasswordUser.text.toString()
            val confirmPassword = binding.editTextConfirmPasswordUser.text.toString()

            val passwordRegex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[^\\s]{8,}\$")

            when {
                mail.isEmpty() -> {
                    Toast.makeText(this, "Ingresa tu correo", Toast.LENGTH_SHORT).show()
                }
                password.isEmpty() || confirmPassword.isEmpty() -> {
                    Toast.makeText(this, "Completa ambos campos de contraseña", Toast.LENGTH_SHORT).show()
                }
                password != confirmPassword -> {
                    Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                }
                !password.matches(passwordRegex) -> {
                    Toast.makeText(this, "La contraseña debe tener al menos 8 caracteres, mayúscula, minúscula y número, sin espacios", Toast.LENGTH_LONG).show()
                }
                else -> {
                    registrarUsuario(mail, password)
                }
            }
        }
    }


    fun registrarUsuario(mail:String, password:String){
        auth.createUserWithEmailAndPassword(mail, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Usuario creado correctamente
                    val intentRegistro = Intent(context, Login_activity::class.java)
                    startActivity(intentRegistro)
                } else {
                    // El usuario no se pudo crear
                    Toast.makeText(
                        baseContext,
                        "No se pudo registrar",
                        Toast.LENGTH_LONG,
                    ).show()
                }
            }
    }

}