package com.example.agendadam.ui // Tu paquete ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.agendadam.AgendaApp
import com.example.agendadam.MainActivity
import com.example.agendadam.databinding.ActivityLoginBinding
import com.example.agendadam.model.Usuario
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = (application as AgendaApp).database
        val usuarioDao = database.usuarioDao()

        binding.btnRegistro.setOnClickListener {
            val nombre = binding.etNombreUsuario.text.toString()
            val pass = binding.etPassword.text.toString()
            val email = binding.etEmail.text.toString()

            if (nombre.isNotEmpty() && pass.isNotEmpty() && email.isNotEmpty()) {
                lifecycleScope.launch {
                    val existe = usuarioDao.buscarPorNombre(nombre)
                    if (existe == null) {
                        val nuevoUsuario = Usuario(nombreUsuario = nombre, contrasena = pass, email = email)
                        usuarioDao.insertarUsuario(nuevoUsuario)
                        mostrarMensaje("Usuario registrado correctamente")
                    } else {
                        mostrarMensaje("El usuario ya existe")
                    }
                }
            } else {
                mostrarMensaje("Rellena todos los campos para registrarte")
            }
        }

        binding.btnLogin.setOnClickListener {
            val nombre = binding.etNombreUsuario.text.toString()
            val pass = binding.etPassword.text.toString()

            if (nombre.isNotEmpty() && pass.isNotEmpty()) {
                lifecycleScope.launch {
                    val usuario = usuarioDao.login(nombre, pass) // Consulta a BD
                    if (usuario != null) {
                        irAMainActivity(usuario.nombreUsuario)
                    } else {
                        mostrarMensaje("Credenciales incorrectas")
                    }
                }
            } else {
                mostrarMensaje("Rellena usuario y contraseña")
            }
        }
    }

    private fun irAMainActivity(nombreUsuario: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("NOMBRE_USUARIO", nombreUsuario)
        startActivity(intent)
        finish()
    }

    private fun mostrarMensaje(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }
}