package com.example.retrofitapp.sources

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.retrofitapp.R
import com.example.retrofitapp.databinding.ItemDetailBinding
import com.example.retrofitapp.sources.character.data.Results
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ItemDetailActivity : AppCompatActivity() {

    private lateinit var binding: ItemDetailBinding
    private lateinit var episodeAdapter: EpisodeAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ItemDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.episodesRV

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        episodeAdapter = EpisodeAdapter(emptyList())
        recyclerView.adapter = episodeAdapter

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.api.getResults()
                withContext(Dispatchers.Main) {
                    episodeAdapter.character = response.results
                    episodeAdapter.notifyDataSetChanged()
                    Log.e("RESPONSE", "${response.results}")
                }
            } catch (e: Exception) {
                Log.e("RESPONSE EXCEPTION", "Error getting results of characters", e)
            }
        }
    }
}
