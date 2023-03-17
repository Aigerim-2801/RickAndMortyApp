package com.example.retrofitapp.sources

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofitapp.R
import com.example.retrofitapp.databinding.ItemDetailBinding
import kotlinx.coroutines.*

class ItemDetailActivity : AppCompatActivity() {

    private lateinit var binding: ItemDetailBinding
    private lateinit var episodeAdapter: EpisodeAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ItemDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(findViewById(R.id.toolbar))
        binding.toolbarLayout.title = title


        recyclerView = binding.contentCharacter.episodesRV

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        episodeAdapter = EpisodeAdapter(emptyList())
        recyclerView.adapter = episodeAdapter


        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.api.getResults()
                withContext(Dispatchers.Main) {
                    episodeAdapter.character = response.results

                    val dividerItemDecoration = DividerItemDecoration(recyclerView.context, layoutManager.orientation)
                    dividerItemDecoration.setDrawable(ResourcesCompat.getDrawable(resources, R.drawable.divider_episode_rv, null)!!)
                    recyclerView.addItemDecoration(dividerItemDecoration)

                    for(position in response.results) {
                        binding.contentCharacter.typed.text = position.type
                    }

                    episodeAdapter.notifyDataSetChanged()
                    Log.e("RESPONSE", "${response.results}")
                }
            } catch (e: Exception) {
                Log.e("RESPONSE EXCEPTION", "Error getting results of characters", e)
            }
        }
    }


}
