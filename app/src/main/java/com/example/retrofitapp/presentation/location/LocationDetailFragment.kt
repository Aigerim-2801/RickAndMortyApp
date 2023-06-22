package com.example.retrofitapp.presentation.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.retrofitapp.R
import com.example.retrofitapp.databinding.LocationDetailBinding
import com.example.retrofitapp.adapters.CharacterAdapter
import com.example.retrofitapp.data.utils.Const
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LocationDetailFragment : Fragment() {

    private var _binding: LocationDetailBinding?= null
    private val binding get() = _binding!!

    private val characterAdapter = CharacterAdapter()

    @Inject
    lateinit var assistedFactory: LocationDetailViewModel.LocationDetailFactory

    private val viewModel: LocationDetailViewModel by viewModels {
        LocationDetailViewModel.LocationDetailViewModelFactory(
            assistedFactory,
            arguments?.getInt(Const.LOCATION_ID, -1) ?: -1
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LocationDetailBinding.inflate(inflater, container, false)

        val layoutManager = GridLayoutManager(requireContext(), 2)
        binding.locationCharacterRv.layoutManager = layoutManager
        binding.locationCharacterRv.adapter = characterAdapter

        viewModel.characterMutableLiveData.observe(viewLifecycleOwner) {
            characterAdapter.submitList(it)
        }

        observeCharacter()

        characterAdapter.onCharacterClick = { character->
            val bundle = Bundle().apply {
                putInt(Const.CHARACTER_ID, character.id)
            }
            val navController = findNavController()
            navController.navigate(R.id.action_locationDetailFragment_to_characterDetailFragment, bundle)
        }

        return binding.root
    }

    private fun observeCharacter() {
        viewModel.locations.observe(viewLifecycleOwner) { locations ->
            binding.typeLocationDetail.text = locations.type
            binding.nameLocationDetail.text = locations.name
            binding.dimensionLocationDetail.text = locations.dimension
        }
    }
}
