package com.example.retrofitapp.presentation.character

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.retrofitapp.presentation.episode.EpisodeDetailViewModel
import com.example.retrofitapp.presentation.location.LocationDetailViewModel

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