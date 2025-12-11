package com.example.agendadam.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.agendadam.AgendaApp
import com.example.agendadam.databinding.FragmentPerfilBinding
import com.example.agendadam.model.Usuario
import kotlinx.coroutines.launch

class PerfilFragment : Fragment() {

    private lateinit var binding: FragmentPerfilBinding
    private var usuarioActual: Usuario? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPerfilBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nombreUsuario = requireActivity().intent.getStringExtra("NOMBRE_USUARIO")

        if (nombreUsuario != null) {
            cargarDatosUsuario(nombreUsuario)
        }

        // 2. Botón Guardar
        binding.btnActualizarPerfil.setOnClickListener {
            actualizarDatos()
        }
    }

    private fun cargarDatosUsuario(nombre: String) {
        val database = (requireActivity().application as AgendaApp).database

        lifecycleScope.launch {
            // Buscamos al usuario en la BD
            usuarioActual = database.usuarioDao().buscarPorNombre(nombre)

            usuarioActual?.let { user ->
                binding.tvUsernameValue.text = user.nombreUsuario
                binding.etEmailPerfil.setText(user.email)
                binding.etPasswordPerfil.setText(user.contrasena)
            }
        }
    }

    private fun actualizarDatos() {
        val nuevoEmail = binding.etEmailPerfil.text.toString()
        val nuevaPass = binding.etPasswordPerfil.text.toString()

        if (usuarioActual != null && nuevoEmail.isNotEmpty() && nuevaPass.isNotEmpty()) {
            val database = (requireActivity().application as AgendaApp).database

            val usuarioActualizado = usuarioActual!!.copy(
                email = nuevoEmail,
                contrasena = nuevaPass
            )

            lifecycleScope.launch {
                database.usuarioDao().actualizarUsuario(usuarioActualizado)
                Toast.makeText(context, "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show()

                usuarioActual = usuarioActualizado
            }
        } else {
            Toast.makeText(context, "Error: Campos vacíos", Toast.LENGTH_SHORT).show()
        }
    }
}