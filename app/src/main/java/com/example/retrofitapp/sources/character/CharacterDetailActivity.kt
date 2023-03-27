package com.example.retrofitapp.sources.character

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.retrofitapp.R
import com.example.retrofitapp.databinding.ItemDetailBinding
import com.example.retrofitapp.sources.RetrofitInstance
import com.example.retrofitapp.sources.character.data.ResultsCharacter
import com.example.retrofitapp.sources.episode.EpisodeCharacterActivity
import com.example.retrofitapp.sources.location.LocationCharacterActivity
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CharacterDetailActivity : AppCompatActivity() {

    private lateinit var binding: ItemDetailBinding
    private lateinit var episodeAdapter: EpisodeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ItemDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val layoutManager = LinearLayoutManager(this)
        binding.contentCharacter.episodesRV.layoutManager = layoutManager
        episodeAdapter = EpisodeAdapter(emptyList())
        binding.contentCharacter.episodesRV.adapter = episodeAdapter


        episodeAdapter.onEpisodenClick = {
            val intent = Intent(this, EpisodeCharacterActivity::class.java).apply {
                putExtra("episode_id", it.id)
            }
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val characterId = intent.getIntExtra("character_id", -1)
                RetrofitInstance.api_character.getCharacterInfo(characterId).enqueue(object : Callback<ResultsCharacter> {
                    override fun onResponse(call: Call<ResultsCharacter>, response: Response<ResultsCharacter>) {
                        val characterInfo = response.body()

                        Glide.with(this@CharacterDetailActivity).load(characterInfo?.image)
                            .into(binding.imaged)
                        binding.named.text = characterInfo?.name
                        binding.statusd.text = characterInfo?.status
                        binding.speciesd.text = characterInfo?.species
                        binding.contentCharacter.genderd.text = characterInfo?.gender
                        binding.contentCharacter.origind.text = characterInfo?.origin?.name
                        binding.contentCharacter.locationName.text = characterInfo?.location?.name
                        binding.contentCharacter.typed.text =
                            if (characterInfo?.type.isNullOrEmpty()) { "Unknown" } else { characterInfo?.type }

                        binding.contentCharacter.episodesRV.adapter = episodeAdapter.apply {
                            episodes = characterInfo?.episode ?: emptyList()
                        }

                        binding.contentCharacter.btnLocation.setOnClickListener {
                            val intent = Intent(this@CharacterDetailActivity, LocationCharacterActivity::class.java).apply {
                                val locationId = characterInfo?.location?.url?.let { it1 -> getIdUrl(it1) }
                                putExtra("location_id", locationId?.toInt())
                            }
                            startActivity(intent)
                        }

                    }

                    override fun onFailure(call: Call<ResultsCharacter>, t: Throwable) {
                    }
                })


                val response = RetrofitInstance.api_character.getCharacter()
                withContext(Dispatchers.Main) {

                    val dividerItemDecoration = DividerItemDecoration(binding.contentCharacter.episodesRV.context, layoutManager.orientation)
                    dividerItemDecoration.setDrawable(ResourcesCompat.getDrawable(resources, R.drawable.divider_episode_rv, null)!!)
                    binding.contentCharacter.episodesRV.addItemDecoration(dividerItemDecoration)
                    episodeAdapter.notifyDataSetChanged()
                    Log.e("RESPONSE", "${response.results}")
                }
            } catch (e: Exception) {
                Log.e("RESPONSE EXCEPTION", "Error getting results of characters", e)
            }
        }
    }


    private fun getIdUrl(url: String): String {
        return url.substring(url.lastIndexOf("/") + 1)
    }
}
