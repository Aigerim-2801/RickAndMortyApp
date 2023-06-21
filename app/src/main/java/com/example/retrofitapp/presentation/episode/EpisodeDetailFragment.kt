package com.example.retrofitapp.presentation.episode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.retrofitapp.R
import com.example.retrofitapp.databinding.EpisodeDetailBinding
import com.example.retrofitapp.adapters.CharacterAdapter
import com.example.retrofitapp.data.utils.Const
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

        val episodeId = arguments?.getInt(Const.EPISODE_ID, -1) ?: -1

        val viewModelFactory = ViewModelFactory(episodeId, requireContext())
        viewModel = ViewModelProvider(this, viewModelFactory)[EpisodeDetailViewModel::class.java]

        viewModel.characterMutableLiveData.observe(viewLifecycleOwner) {
            characterAdapter.submitList(it)
        }

        observeCharacter()

        characterAdapter.onCharacterClick = { character->
            val bundle = Bundle().apply {
                putInt(Const.CHARACTER_ID, character.id)
            }
            val navController = findNavController()
            navController.navigate(R.id.action_episodeDetailFragment_to_characterDetailFragment, bundle)
        }

        return binding.root
    }

    private fun observeCharacter() {
        viewModel.episodes.observe(viewLifecycleOwner) { episodes ->
            binding.nameEpisodeDetail.text = episodes.name
            binding.airDateEpisodeDetail.text = episodes.air_date
            binding.episodeEpisodeDetail.text = episodes.episode
        }
    }
}