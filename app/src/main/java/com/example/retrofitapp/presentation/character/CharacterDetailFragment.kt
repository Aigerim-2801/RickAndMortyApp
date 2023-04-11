package com.example.retrofitapp.presentation.character

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.retrofitapp.R
import com.example.retrofitapp.presentation.episode.EpisodeDetailFragment
import com.example.retrofitapp.presentation.location.LocationDetailFragment
import com.example.retrofitapp.adapters.EpisodeAdapter
import com.example.retrofitapp.data.repository.ApiResult
import com.example.retrofitapp.databinding.CharacterDetailBinding

class CharacterDetailFragment : Fragment() {

    private var _binding: CharacterDetailBinding ?= null
    private val binding get() = _binding!!

    private val episodeAdapter = EpisodeAdapter()
    private lateinit var viewModel: CharacterDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CharacterDetailBinding.inflate(inflater, container, false)

        val layoutManager = LinearLayoutManager(requireContext())
        binding.contentCharacter.episodesRV.layoutManager = layoutManager
        binding.contentCharacter.episodesRV.adapter = episodeAdapter

        val characterId = arguments?.getInt(CHARACTER_ID, -1) ?: -1

        val viewModelFactory = ViewModelFactory(characterId)
        viewModel = ViewModelProvider(this, viewModelFactory)[CharacterDetailViewModel::class.java]

        viewModel.episodes.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ApiResult.Success -> {
                    episodeAdapter.submitList(result.value)
                }
                is ApiResult.Error -> {
                    val errorMessage = result.message
                    val throwable = result.throwable
                    Log.e("CharacterDetailFragment", "Error getting episodes of character info: $errorMessage", throwable)
                }
            }
        }

        viewModel.isEpisodeLoading.observe(viewLifecycleOwner){ isLoading ->
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
                R.drawable.divider_rv,
                null
            )!!
        )
        binding.contentCharacter.episodesRV.addItemDecoration(dividerItemDecoration)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun navigateToEpisode(episodeId: Int) {
        val fragment = EpisodeDetailFragment.startEpisodeFragment(episodeId)
        parentFragmentManager.beginTransaction()
            .replace(R.id.recycler_view_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun navigateToLocation(locationId: Int) {
        val fragment = LocationDetailFragment.startLocationFragment(locationId)
        parentFragmentManager.beginTransaction()
            .replace(R.id.recycler_view_container, fragment)
            .addToBackStack(null)
            .commit()
    }


    private fun observeCharacter() {
        viewModel.characterInfoLiveData.observe(viewLifecycleOwner) { character ->
            Glide.with(this@CharacterDetailFragment).load(character.image)
                .into(binding.imageCharacter)
            binding.nameCharacter.text = character.name
            binding.statusCharacter.text = character.status
            binding.speciesCharacter.text = character.species
            binding.contentCharacter.genderCharacter.text = character.gender
            binding.contentCharacter.originCharacter.text = character.origin.name
            binding.contentCharacter.locationName.text = character.location.name
            binding.contentCharacter.typeCharacter.text =
                character.type.let { viewModel.typeCharacter(it) }

            binding.contentCharacter.btnLocation.setOnClickListener {
                val locationId = viewModel.getIdUrl(character.location.url)
                 navigateToLocation(locationId.toInt())
            }

        }
    }

    companion object {
        private const val CHARACTER_ID = "character_id"
        fun startCharacterFragment(characterId: Int): CharacterDetailFragment {
            val args = Bundle().apply {
                putInt(CHARACTER_ID, characterId)
            }
            return CharacterDetailFragment().apply {
                arguments = args
            }
        }
    }
}
