package com.example.retrofitapp.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.retrofitapp.data.utils.CharactersDatabase
import com.example.retrofitapp.domain.model.character.ResultsCharacter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class FavoriteViewModel(private val context: Context) : ViewModel() {

    private val _favoriteCharactersMutableStateFlow = MutableStateFlow<List<ResultsCharacter>>(emptyList())
    val favoriteCharactersStateFlow: StateFlow<List<ResultsCharacter>> = _favoriteCharactersMutableStateFlow

    private val dao = Room.databaseBuilder(context, CharactersDatabase::class.java, "CharactersFavoriteDatabase").allowMainThreadQueries().build().getCharactersDao()

    init {
        fetchFavoriteCharacters()
    }

    private fun fetchFavoriteCharacters() {
        viewModelScope.launch {
            _favoriteCharactersMutableStateFlow.value = dao.getAll()
        }
    }

    fun onFavoriteStateChanged(isFavorite: Boolean, character: ResultsCharacter){
        viewModelScope.launch {
            if (!isFavorite) {
                character.isFavorite = true
                dao.insert(character)
            } else {
                character.isFavorite = false
                dao.delete(character)
            }
            fetchFavoriteCharacters()
        }
    }
}