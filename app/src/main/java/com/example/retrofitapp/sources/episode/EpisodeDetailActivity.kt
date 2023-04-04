package com.example.retrofitapp.sources.episode

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.retrofitapp.databinding.EpisodeCharacterBinding
import com.example.retrofitapp.sources.character.CharacterAdapter
import com.example.retrofitapp.sources.character.CharacterDetailActivity
import com.example.retrofitapp.sources.character.viewModel.ViewModelFactory
import com.example.retrofitapp.sources.episode.viewModel.EpisodeDetailViewModel

class EpisodeDetailActivity : AppCompatActivity() {

    private lateinit var binding: EpisodeCharacterBinding
    private val characterAdapter = CharacterAdapter()

    private lateinit var viewModel: EpisodeDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EpisodeCharacterBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val layoutManager = GridLayoutManager(this, 2)
        binding.episodeCharacterRv.layoutManager = layoutManager
        binding.episodeCharacterRv.adapter = characterAdapter


        val episodeId = intent.getIntExtra(EPISODE_ID, -1)
        val viewModelFactory = ViewModelFactory(episodeId)
        viewModel = ViewModelProvider(this, viewModelFactory)[EpisodeDetailViewModel::class.java]

        viewModel.characterMutableLiveData.observe(this) {
            characterAdapter.submitList(it)
        }

        observeCharacter()

        characterAdapter.onCharacterClick = { navigateToCharacterDetail(it.id) }

    }

    private fun navigateToCharacterDetail(id: Int) {
        val intent = CharacterDetailActivity.startCharacterDetailIntent(this, id)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun observeCharacter() {
        viewModel.episodes.observe(this) { episodes ->
            binding.nameEpisodeCharacter.text = episodes.name
            binding.airDateEpisodeCharacter.text = episodes.air_date
            binding.episodeEpisodeCharacter.text = episodes.episode
        }
    }

    companion object {
        private const val EPISODE_ID = "episode_id"
        fun startEpisodeIntent(context: Context, episodeId: Int): Intent {
            return Intent(context, EpisodeDetailActivity::class.java).apply {
                putExtra(EPISODE_ID, episodeId)
            }
        }
    }

}