package com.example.retrofitapp.presentation.episode

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.retrofitapp.databinding.EpisodeDetailBinding
import com.example.retrofitapp.presentation.character.CharacterDetailActivity
import com.example.retrofitapp.adapters.CharacterAdapter
import com.example.retrofitapp.presentation.character.ViewModelFactory

class EpisodeDetailActivity : AppCompatActivity() {

    private lateinit var binding: EpisodeDetailBinding
    private val characterAdapter = CharacterAdapter()

    private lateinit var viewModel: EpisodeDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EpisodeDetailBinding.inflate(layoutInflater)
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
            binding.nameEpisodeDetail.text = episodes.name
            binding.airDateEpisodeDetail.text = episodes.air_date
            binding.episodeEpisodeDetail.text = episodes.episode
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