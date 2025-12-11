package com.example.agendadam.ui

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agendadam.AgendaApp
import com.example.agendadam.adapter.TareasAdapter
import com.example.agendadam.databinding.DialogTareaBinding
import com.example.agendadam.databinding.FragmentTareasBinding
import com.example.agendadam.model.Tarea
import kotlinx.coroutines.launch

class TareasFragment : Fragment() {

    private lateinit var binding: FragmentTareasBinding
    private lateinit var adapter: TareasAdapter
    private val usuarioActual = "UsuarioPrueba"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTareasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configurarRecycler()

        configurarBuscador()

        observarTareas()

        binding.fabAddTarea.setOnClickListener {
            mostrarDialogoTarea(null)
        }
    }
    // ------------------------------------------------

    private fun configurarBuscador() {
        binding.svTareas.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean { return false }

            override fun onQueryTextChange(newText: String?): Boolean {
                val consulta = newText ?: ""
                buscarTareasEnBD(consulta) // Llamamos a la búsqueda en BD
                return true
            }
        })
    }

    private fun buscarTareasEnBD(consulta: String) {
        val database = (requireActivity().application as AgendaApp).database
        // Al usar Flow, la búsqueda se actualiza sola
        lifecycleScope.launch {
            database.tareaDao().buscarTareas(usuarioActual, consulta).collect { listaFiltrada ->
                adapter.setTareas(listaFiltrada)
            }
        }
    }


    private fun configurarRecycler() {
        adapter = TareasAdapter(
            onTareaClick = { tarea -> mostrarDialogoTarea(tarea) },
            onTareaBorrar = { tarea -> borrarTarea(tarea) },
            onTareaCompartir = { tarea -> compartirTarea(tarea) }
        )
        binding.rvTareas.layoutManager = LinearLayoutManager(context)
        binding.rvTareas.adapter = adapter
    }

    private fun observarTareas() {
        // Solo observamos todas las tareas si el buscador está vacío o al inicio
        if (binding.svTareas.query.isNullOrEmpty()) {
            val database = (requireActivity().application as AgendaApp).database
            lifecycleScope.launch {
                database.tareaDao().obtenerTareasPorUsuario(usuarioActual).collect { listaTareas ->
                    adapter.setTareas(listaTareas)
                }
            }
        }
    }


    private fun compartirTarea(tarea: Tarea) {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Tarea pendiente: ${tarea.titulo} (Prioridad: ${tarea.prioridad})")
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(intent, "Compartir tarea con...")
        startActivity(shareIntent)
    }

    private fun mostrarDialogoTarea(tareaEditar: Tarea?) {
        val dialogBinding = DialogTareaBinding.inflate(layoutInflater)

        val prioridades = listOf("Alta", "Media", "Baja")
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, prioridades)
        dialogBinding.spinnerPrioridad.adapter = spinnerAdapter

        dialogBinding.sbDificultad.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                dialogBinding.tvDificultadLabel.text = "Dificultad: $progress"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        if (tareaEditar != null) {
            dialogBinding.tvTituloDialogo.text = "Editar Tarea"
            dialogBinding.etTituloTarea.setText(tareaEditar.titulo)
            dialogBinding.etDescripcionTarea.setText(tareaEditar.descripcion)
            dialogBinding.sbDificultad.progress = tareaEditar.dificultad
            dialogBinding.tvDificultadLabel.text = "Dificultad: ${tareaEditar.dificultad}"
            val spinnerPos = prioridades.indexOf(tareaEditar.prioridad)
            if (spinnerPos >= 0) dialogBinding.spinnerPrioridad.setSelection(spinnerPos)
        }

        AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .setPositiveButton("Guardar") { _, _ ->
                val titulo = dialogBinding.etTituloTarea.text.toString()
                val desc = dialogBinding.etDescripcionTarea.text.toString()
                val prioridad = dialogBinding.spinnerPrioridad.selectedItem.toString()
                val dificultad = dialogBinding.sbDificultad.progress

                if (titulo.isNotEmpty()) {
                    guardarOActualizarTarea(tareaEditar, titulo, desc, prioridad, dificultad)
                } else {
                    Toast.makeText(context, "El título es obligatorio", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun guardarOActualizarTarea(tareaOriginal: Tarea?, titulo: String, desc: String, prio: String, dif: Int) {
        val database = (requireActivity().application as AgendaApp).database
        lifecycleScope.launch {
            if (tareaOriginal == null) {
                val nuevaTarea = Tarea(titulo = titulo, descripcion = desc, prioridad = prio, dificultad = dif, usuarioPropietario = usuarioActual)
                database.tareaDao().insertar(nuevaTarea)
            } else {
                val tareaActualizada = tareaOriginal.copy(titulo = titulo, descripcion = desc, prioridad = prio, dificultad = dif)
                database.tareaDao().actualizar(tareaActualizada)
            }
        }
    }

    private fun borrarTarea(tarea: Tarea) {
        val database = (requireActivity().application as AgendaApp).database
        lifecycleScope.launch {
            database.tareaDao().eliminar(tarea)
            Toast.makeText(context, "Tarea eliminada", Toast.LENGTH_SHORT).show()
        }
    }
}