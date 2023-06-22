package com.example.retrofitapp.presentation.favorite

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.retrofitapp.data.repository.DatabaseRepository
import com.example.retrofitapp.data.utils.CharactersDatabase
import com.example.retrofitapp.domain.model.character.ResultsCharacter
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FavoriteViewModel @Inject constructor(private val databaseRepository: DatabaseRepository) : ViewModel() {

    private val _favoriteCharactersMutableStateFlow = MutableStateFlow<List<ResultsCharacter>>(emptyList())
    val favoriteCharactersStateFlow: StateFlow<List<ResultsCharacter>> = _favoriteCharactersMutableStateFlow

    init {
        fetchFavoriteCharacters()
    }

    private fun fetchFavoriteCharacters() {
        viewModelScope.launch {
            _favoriteCharactersMutableStateFlow.value = databaseRepository.getAll()
        }
    }

    fun onFavoriteStateChanged(isFavorite: Boolean, character: ResultsCharacter){
        viewModelScope.launch {
            if (!isFavorite) {
                character.isFavorite = true
                databaseRepository.insert(character)
            } else {
                character.isFavorite = false
                databaseRepository.delete(character)
            }
            fetchFavoriteCharacters()
        }
    }
}