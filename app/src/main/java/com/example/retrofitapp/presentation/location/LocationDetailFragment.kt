package com.example.retrofitapp.presentation.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.retrofitapp.R
import com.example.retrofitapp.databinding.LocationDetailBinding
import com.example.retrofitapp.adapters.CharacterAdapter
import com.example.retrofitapp.presentation.character.CharacterDetailFragment
import com.example.retrofitapp.presentation.character.ViewModelFactory

class LocationDetailFragment : Fragment() {

    private var _binding: LocationDetailBinding?= null
    private val binding get() = _binding!!

    private val characterAdapter = CharacterAdapter()

    private lateinit var viewModel: LocationDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LocationDetailBinding.inflate(inflater, container, false)

        val layoutManager = GridLayoutManager(requireContext(), 2)
        binding.locationCharacterRv.layoutManager = layoutManager
        binding.locationCharacterRv.adapter = characterAdapter

        val locationId = arguments?.getInt(LOCATION_ID, -1) ?: -1

        val viewModelFactory = ViewModelFactory(locationId)
        viewModel = ViewModelProvider(this, viewModelFactory)[LocationDetailViewModel::class.java]

        viewModel.characterMutableLiveData.observe(viewLifecycleOwner) {
            characterAdapter.submitList(it)
        }

        observeCharacter()

        characterAdapter.onCharacterClick = { navigateToCharacterDetail(it.id) }

        return binding.root
    }

    private fun observeCharacter() {
        viewModel.locations.observe(viewLifecycleOwner) { locations ->
            binding.typeLocationDetail.text = locations.type
            binding.nameLocationDetail.text = locations.name
            binding.dimensionLocationDetail.text = locations.dimension
        }
    }

    private fun navigateToCharacterDetail(id: Int){
        val fragment = CharacterDetailFragment.startCharacterFragment(id)
        parentFragmentManager.beginTransaction()
            .replace(R.id.recycler_view_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    companion object {
        private const val LOCATION_ID = "location_id"
        fun startLocationFragment(locationId: Int): LocationDetailFragment {
            val args = Bundle().apply {
                putInt(LOCATION_ID, locationId)
            }
            return LocationDetailFragment().apply {
                arguments = args
            }
        }
    }
}
