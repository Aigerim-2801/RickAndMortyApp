package com.example.retrofitapp.presentation.episode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.retrofitapp.R
import com.example.retrofitapp.databinding.EpisodeDetailBinding
import com.example.retrofitapp.adapters.CharacterAdapter
import com.example.retrofitapp.presentation.character.CharacterDetailFragment
import com.example.retrofitapp.presentation.character.ViewModelFactory

class EpisodeDetailFragment : Fragment() {

    private var _binding: EpisodeDetailBinding?= null
    private val binding get() = _binding!!

    private val characterAdapter = CharacterAdapter()

    private lateinit var viewModel: EpisodeDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = EpisodeDetailBinding.inflate(inflater, container, false)

        val layoutManager = GridLayoutManager(requireContext(), 2)
        binding.episodeCharacterRv.layoutManager = layoutManager
        binding.episodeCharacterRv.adapter = characterAdapter

        val episodeId = arguments?.getInt(EPISODE_ID, -1) ?: -1

        val viewModelFactory = ViewModelFactory(episodeId)
        viewModel = ViewModelProvider(this, viewModelFactory)[EpisodeDetailViewModel::class.java]

        viewModel.characterMutableLiveData.observe(viewLifecycleOwner) {
            characterAdapter.submitList(it)
        }

        observeCharacter()

        characterAdapter.onCharacterClick = { navigateToCharacterDetail(it.id) }

        return binding.root
    }

    private fun navigateToCharacterDetail(id: Int) {
        val fragment = CharacterDetailFragment.startCharacterFragment(id)
        parentFragmentManager.beginTransaction()
            .replace(R.id.recycler_view_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun observeCharacter() {
        viewModel.episodes.observe(viewLifecycleOwner) { episodes ->
            binding.nameEpisodeDetail.text = episodes.name
            binding.airDateEpisodeDetail.text = episodes.air_date
            binding.episodeEpisodeDetail.text = episodes.episode
        }
    }

    companion object {
        private const val EPISODE_ID = "episode_id"
        fun startEpisodeFragment(episodeId: Int): EpisodeDetailFragment {
            val args = Bundle().apply {
                putInt(EPISODE_ID, episodeId)
            }
            return EpisodeDetailFragment().apply {
                arguments = args
            }
        }
    }
}