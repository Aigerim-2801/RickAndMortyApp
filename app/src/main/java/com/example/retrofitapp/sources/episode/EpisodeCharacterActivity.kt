package com.example.retrofitapp.sources.episode

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.retrofitapp.databinding.EpisodeCharacterBinding
import com.example.retrofitapp.sources.RetrofitInstance
import com.example.retrofitapp.sources.episode.data.ResultsEpisode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EpisodeCharacterActivity : AppCompatActivity() {

    private lateinit var binding: EpisodeCharacterBinding
    private lateinit var episodeCharacterAdapter: EpisodeCharacterAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EpisodeCharacterBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val layoutManager = GridLayoutManager(this, 2)
        binding.episodeCharacterRv.layoutManager = layoutManager
        episodeCharacterAdapter = EpisodeCharacterAdapter(emptyList())
        binding.episodeCharacterRv.adapter = episodeCharacterAdapter


        GlobalScope.launch(Dispatchers.IO) {
            try {
                val episodeId = intent.getIntExtra("episode_id", -1)
                RetrofitInstance.api_episode.getEpisodeInfo(episodeId).enqueue(object :
                    Callback<ResultsEpisode> {
                    override fun onResponse(call: Call<ResultsEpisode>, response: Response<ResultsEpisode>) {
                        val episodeInfo = response.body()

                        binding.nameEpisodeCharacter.text = episodeInfo?.name
                        binding.airDateEpisodeCharacter.text = episodeInfo?.air_date
                        binding.episodeEpisodeCharacter.text = episodeInfo?.episode

                        binding.episodeCharacterRv.adapter = episodeCharacterAdapter.apply {
                            charactere = episodeInfo?.characters ?: emptyList()
                        }

                    }

                    override fun onFailure(call: Call<ResultsEpisode>, t: Throwable) {
                    }
                })


                val response = RetrofitInstance.api_episode.getEpisode()
                withContext(Dispatchers.Main) {

                    episodeCharacterAdapter.notifyDataSetChanged()
                    Log.e("RESPONSE", "${response.results}")
                }
            } catch (e: Exception) {
                Log.e("RESPONSE EXCEPTION", "Error getting results of characters of episode", e)
            }
        }
    }


}
