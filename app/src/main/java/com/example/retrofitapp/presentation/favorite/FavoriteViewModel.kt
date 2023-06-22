package com.example.retrofitapp.presentation.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.retrofitapp.data.repository.DatabaseRepository
import com.example.retrofitapp.domain.model.character.ResultsCharacter
import dagger.hilt.android.lifecycle.HiltViewModel
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