package com.example.agendadam.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agendadam.adapter.DogAdapter
import com.example.agendadam.databinding.FragmentApiBinding
import com.example.agendadam.model.DogsResponse
import com.example.agendadam.network.APIService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiFragment : Fragment() {

    private lateinit var binding: FragmentApiBinding
    private lateinit var adapter: DogAdapter
    private val dogImages = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentApiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        searchImages()
    }

    private fun initRecyclerView() {
        adapter = DogAdapter(dogImages)
        binding.rvDogs.layoutManager = LinearLayoutManager(context)
        binding.rvDogs.adapter = adapter
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://dog.ceo/api/breed/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun searchImages() {
        val apiService = getRetrofit().create(APIService::class.java)

        val call = apiService.getDogsByBreeds("hound/images")

        call.enqueue(object : Callback<DogsResponse> {
            override fun onResponse(call: Call<DogsResponse>, response: Response<DogsResponse>) {
                if (response.isSuccessful) {
                    val images = response.body()?.images ?: emptyList()

                    dogImages.clear()
                    dogImages.addAll(images)

                    adapter.notifyDataSetChanged()
                } else {
                    showError()
                }
            }

            override fun onFailure(call: Call<DogsResponse>, t: Throwable) {
                showError()
                Log.e("API_ERROR", "Error: ${t.message}")
            }
        })
    }

    private fun showError() {
        Toast.makeText(context, "Ha ocurrido un error al cargar las imágenes", Toast.LENGTH_SHORT).show()
    }
}