package com.example.retrofitapp.presentation.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.retrofitapp.data.repository.DatabaseRepository
import com.example.retrofitapp.domain.model.character.ResultsCharacter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class FavoriteViewModel (private val databaseRepository: DatabaseRepository) : ViewModel() {

    private val _favoriteCharactersMutableStateFlow = MutableStateFlow<List<ResultsCharacter>>(emptyList())
    val favoriteCharactersStateFlow: StateFlow<List<ResultsCharacter>> = _favoriteCharactersMutableStateFlow

    init {
        fetchFavoriteCharacters()
    }

    //usecases instead of using directly repo
    //io thread
    //compose ui
    //unit tests
    //show cached data from room if don't have internet
    //not empty screen=
    //release build-
    //imageloader-
    //for error not use log,

    //
    private fun fetchFavoriteCharacters() {
        viewModelScope.launch {
            databaseRepository.getAll().collect{
                _favoriteCharactersMutableStateFlow.value = it
            }
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