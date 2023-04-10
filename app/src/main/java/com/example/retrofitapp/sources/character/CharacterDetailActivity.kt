package com.example.retrofitapp.sources.character

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.retrofitapp.R
import com.example.retrofitapp.databinding.CharacterDetailBinding
import com.example.retrofitapp.sources.character.viewModel.CharacterDetailViewModel
import com.example.retrofitapp.sources.character.viewModel.ViewModelFactory
import com.example.retrofitapp.sources.episode.EpisodeAdapter
import com.example.retrofitapp.sources.episode.EpisodeDetailActivity
import com.example.retrofitapp.sources.location.LocationDetailActivity
import com.example.retrofitapp.sources.repository.ApiResult

class CharacterDetailActivity : AppCompatActivity() {

    private lateinit var binding: CharacterDetailBinding
    private val episodeAdapter = EpisodeAdapter()
    private lateinit var viewModel: CharacterDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CharacterDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.contentCharacter.episodesRV.layoutManager = layoutManager
        binding.contentCharacter.episodesRV.adapter = episodeAdapter


        val characterId = intent.getIntExtra(CHARACTER_ID, -1)
        val viewModelFactory = ViewModelFactory(characterId)
        viewModel = ViewModelProvider(this, viewModelFactory)[CharacterDetailViewModel::class.java]

        viewModel.episodes.observe(this) { result ->
            when (result) {
                is ApiResult.Success -> {
                    episodeAdapter.submit(result.value)
                }
                is ApiResult.Error -> {
                    val errorMessage = result.message
                    val throwable = result.throwable
                    Log.e("CharacterDetailActivity", "Error getting episodes of character info: $errorMessage", throwable)
                }
            }
        }

        viewModel.isEpisodeLoading.observe(this){ isLoading ->
            binding.contentCharacter.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
        }

        observeCharacter()

        episodeAdapter.onEpisodeClick = { navigateToEpisode(it.id) }

        val dividerItemDecoration = DividerItemDecoration(
            binding.contentCharacter.episodesRV.context,
            layoutManager.orientation
        )
        dividerItemDecoration.setDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.divider_episode_rv,
                null
            )!!
        )
        binding.contentCharacter.episodesRV.addItemDecoration(dividerItemDecoration)
    }

    private fun navigateToEpisode(episodeId: Int) {
        val intent = EpisodeDetailActivity.startEpisodeIntent(this, episodeId)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun navigateToLocation(locationId: Int) {
        val intent = LocationDetailActivity.startLocationIntent(this, locationId)
        startActivity(intent)
    }


    private fun observeCharacter() {
        viewModel.characterInfoLiveData.observe(this) { character ->
            Glide.with(this@CharacterDetailActivity).load(character.image)
                .into(binding.imaged)
            binding.named.text = character.name
            binding.statusd.text = character.status
            binding.speciesd.text = character.species
            binding.contentCharacter.genderd.text = character.gender
            binding.contentCharacter.origind.text = character.origin.name
            binding.contentCharacter.locationName.text = character.location.name
            binding.contentCharacter.typed.text =
                character.type.let { viewModel.typeCharacter(it) }

            binding.contentCharacter.btnLocation.setOnClickListener {
                val locationId = viewModel.getIdUrl(character.location.url)
                 navigateToLocation(locationId.toInt())
            }

        }
    }


    companion object {
        private const val CHARACTER_ID = "character_id"
        fun startCharacterDetailIntent(context: Context, characterId: Int): Intent {
            return Intent(context, CharacterDetailActivity::class.java).apply {
                putExtra(CHARACTER_ID, characterId)
            }
        }
    }
}
