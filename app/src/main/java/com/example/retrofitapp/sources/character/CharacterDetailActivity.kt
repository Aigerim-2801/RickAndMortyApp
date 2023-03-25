package com.example.retrofitapp.sources.character

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

//        setSupportActionBar(binding.toolbar)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.setDisplayShowHomeEnabled(true)
//
//
//        binding.appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
//            val totalScrollRange = appBarLayout.totalScrollRange
//
//            val percentage = Math.abs(verticalOffset).toFloat() / totalScrollRange.toFloat()
//
//            binding.toolbar.visibility = if (percentage == 1f) View.VISIBLE else View.GONE
//
//            if (percentage == 1f) {
//                binding.toolbarLayout.title = "SOLP"
//            } else {
//                binding.toolbarLayout.title = ""
//            }
//        })


        val layoutManager = LinearLayoutManager(this)
        binding.contentCharacter.episodesRV.layoutManager = layoutManager
        episodeAdapter = EpisodeAdapter(emptyList())
        binding.contentCharacter.episodesRV.adapter = episodeAdapter


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


}
