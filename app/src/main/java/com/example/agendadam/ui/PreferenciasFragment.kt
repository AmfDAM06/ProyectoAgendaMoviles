package com.example.agendadam.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.agendadam.AgendaApp.Companion.prefs // Importamos la instancia global
import com.example.agendadam.databinding.FragmentPreferenciasBinding

class PreferenciasFragment : Fragment() {

    private lateinit var binding: FragmentPreferenciasBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPreferenciasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val esVip = prefs.getVIP()
        binding.switchVip.isChecked = esVip

        binding.switchVip.setOnCheckedChangeListener { _, isChecked ->
            prefs.saveVIP(isChecked)
            if (isChecked) {
                Toast.makeText(context, "¡Modo VIP activado!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnCerrarSesion.setOnClickListener {

            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}