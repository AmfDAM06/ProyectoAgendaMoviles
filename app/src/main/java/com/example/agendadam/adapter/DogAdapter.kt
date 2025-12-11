package com.example.agendadam.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.agendadam.R
import com.squareup.picasso.Picasso

class DogAdapter(private val images: List<String>) : RecyclerView.Adapter<DogAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_dog, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = images[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = images.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val ivDog = view.findViewById<ImageView>(R.id.ivDog)

        fun bind(image: String) {
            Picasso.get().load(image).into(ivDog)
        }
    }
}