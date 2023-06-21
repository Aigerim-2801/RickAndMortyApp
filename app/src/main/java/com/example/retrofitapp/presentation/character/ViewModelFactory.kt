package com.example.retrofitapp.presentation.character

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.retrofitapp.presentation.FavoriteViewModel
import com.example.retrofitapp.presentation.episode.EpisodeDetailViewModel
import com.example.retrofitapp.presentation.location.LocationDetailViewModel

class ViewModelFactory(private val id: Int, private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CharacterDetailViewModel::class.java)) {
            return CharacterDetailViewModel(id) as T
        } else if (modelClass.isAssignableFrom(EpisodeDetailViewModel::class.java)) {
            return EpisodeDetailViewModel(id) as T
        } else if (modelClass.isAssignableFrom(LocationDetailViewModel::class.java)) {
            return LocationDetailViewModel(id) as T
        }else if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}