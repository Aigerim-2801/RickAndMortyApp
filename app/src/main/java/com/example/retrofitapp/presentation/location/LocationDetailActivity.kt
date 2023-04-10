package com.example.retrofitapp.presentation.location

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.retrofitapp.databinding.LocationDetailBinding
import com.example.retrofitapp.presentation.character.CharacterDetailActivity
import com.example.retrofitapp.adapters.CharacterAdapter
import com.example.retrofitapp.presentation.character.ViewModelFactory

class LocationDetailActivity : AppCompatActivity() {

    private lateinit var binding: LocationDetailBinding
    private val characterAdapter = CharacterAdapter()

    private lateinit var viewModel: LocationDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LocationDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = GridLayoutManager(this, 2)
        binding.locationCharacterRv.layoutManager = layoutManager
        binding.locationCharacterRv.adapter = characterAdapter

        val locationId = intent.getIntExtra(LOCATION_ID, -1)
        val viewModelFactory = ViewModelFactory(locationId)
        viewModel = ViewModelProvider(this, viewModelFactory)[LocationDetailViewModel::class.java]

        viewModel.characterMutableLiveData.observe(this) {
            characterAdapter.submitList(it)
        }

        observeCharacter()

        characterAdapter.onCharacterClick = { navigateToCharacterDetail(it.id) }
    }

    private fun observeCharacter() {
        viewModel.locations.observe(this) { locations ->
            binding.typeLocationDetail.text = locations.type
            binding.nameLocationDetail.text = locations.name
            binding.dimensionLocationDetail.text = locations.dimension
        }
    }

    private fun navigateToCharacterDetail(id: Int){
        val intent = CharacterDetailActivity.startCharacterDetailIntent(this, id)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    companion object {
        private const val LOCATION_ID = "location_id"
        fun startLocationIntent(context: Context, locationId: Int): Intent {
            return Intent(context, LocationDetailActivity::class.java).apply {
                putExtra(LOCATION_ID, locationId)
            }
        }
    }
}
