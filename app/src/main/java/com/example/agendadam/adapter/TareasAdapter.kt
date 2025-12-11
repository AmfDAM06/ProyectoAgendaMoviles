package com.example.agendadam.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.agendadam.R
import com.example.agendadam.databinding.ItemTareaBinding
import com.example.agendadam.model.Tarea

class TareasAdapter(
    private val onTareaClick: (Tarea) -> Unit,
    private val onTareaBorrar: (Tarea) -> Unit,
    private val onTareaCompartir: (Tarea) -> Unit
) : RecyclerView.Adapter<TareasAdapter.TareaViewHolder>() {

    private var listaTareas = emptyList<Tarea>()

    fun setTareas(nuevasTareas: List<Tarea>) {
        this.listaTareas = nuevasTareas
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tarea, parent, false)
        return TareaViewHolder(view)
    }

    override fun onBindViewHolder(holder: TareaViewHolder, position: Int) {
        val tarea = listaTareas[position]
        // Pasamos las 3 funciones al ViewHolder
        holder.bind(tarea, onTareaClick, onTareaBorrar, onTareaCompartir)
    }

    override fun getItemCount(): Int = listaTareas.size

    // Clase interna ViewHolder
    class TareaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemTareaBinding.bind(view)

        fun bind(
            tarea: Tarea,
            onClick: (Tarea) -> Unit,
            onBorrar: (Tarea) -> Unit,
            onCompartir: (Tarea) -> Unit
        ) {
            binding.tvTitulo.text = tarea.titulo
            binding.tvPrioridad.text = "Prioridad: ${tarea.prioridad}"
            binding.cbTerminada.isChecked = tarea.estaTerminada


            itemView.setOnClickListener { onClick(tarea) }

            binding.ivBorrar.setOnClickListener { onBorrar(tarea) }

            binding.ivCompartir.setOnClickListener { onCompartir(tarea) }
        }
    }
}