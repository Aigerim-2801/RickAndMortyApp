package com.example.retrofitapp.sources.character.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.retrofitapp.sources.episode.viewModel.EpisodeDetailViewModel
import com.example.retrofitapp.sources.location.viewModel.LocationDetailViewModel

class ViewModelFactory(private val id: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CharacterDetailViewModel::class.java)) {
            return CharacterDetailViewModel(id) as T
        } else if (modelClass.isAssignableFrom(EpisodeDetailViewModel::class.java)) {
            return EpisodeDetailViewModel(id) as T
        } else if (modelClass.isAssignableFrom(LocationDetailViewModel::class.java)) {
            return LocationDetailViewModel(id) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}