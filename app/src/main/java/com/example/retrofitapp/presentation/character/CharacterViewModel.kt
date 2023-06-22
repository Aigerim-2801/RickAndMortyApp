package com.example.retrofitapp.presentation.character

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.retrofitapp.domain.model.character.FilterCharacters
import com.example.retrofitapp.domain.model.character.ResultsCharacter
import com.example.retrofitapp.data.repository.ApiResult
import com.example.retrofitapp.data.repository.RickAndMortyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterViewModel @Inject constructor(private val rickAndMortyRepository: RickAndMortyRepository) : ViewModel() {

    private var currentPage = 1

    private var isFilterApplied: Boolean = false

    var filterCharacters = FilterCharacters()

    private val _charactersMutableStateFlow = MutableStateFlow<List<ResultsCharacter>>(emptyList())
    val charactersStateFlow: StateFlow<List<ResultsCharacter>> = _charactersMutableStateFlow
    private val updatedList = _charactersMutableStateFlow.value.toMutableList()

    private var favoriteList: List<ResultsCharacter> = listOf()

    init {
        getAllCharacters()
    }

    fun getCharacters(){
        if (!isFilterApplied) {
            getAllCharacters()
        } else {
            getFilteredCharacters(
                name = filterCharacters.name,
                status = filterCharacters.status.toString(),
                gender = filterCharacters.gender.toString(),
                species = filterCharacters.species
            )
        }
    }

      private fun getAllCharacters() {
          isFilterApplied = false
          viewModelScope.launch {
              when (val result = rickAndMortyRepository.getAllCharacters(currentPage)) {
                  is ApiResult.Success -> {
                      if (currentPage == 1) {
                          updatedList.clear()
                      }
                      updatedList.addAll(result.value.results)

                      val currentList = updatedList.toList()
                      val resultedList = currentList.map {
                          favoriteList.firstOrNull { character ->
                              character.id == it.id
                          } ?: it
                      }

                      _charactersMutableStateFlow.value = resultedList

                      currentPage++
                  }
                  is ApiResult.Error -> {
                      val errorMessage = result.message
                      val throwable = result.throwable
                      Log.e(
                          "CharacterViewModel",
                          "Error getting all characters: $errorMessage",
                          throwable
                      )
                  }
              }
          }
      }

    private fun getFilteredCharacters(name: String, status: String, gender: String, species: String) {
        viewModelScope.launch {
        when (val result = rickAndMortyRepository.getFilteredCharacters(name, status, gender, species, currentPage)) {
                is ApiResult.Success -> {
                    if (currentPage == 1) {
                        updatedList.clear()
                    }
                    val tempList = updatedList.toMutableList().apply {
                        addAll(result.value.results)
                    }

                    val currentList = tempList.toList()
                    val resultedList = currentList.map {
                        favoriteList.firstOrNull { character ->
                            character.id == it.id
                        } ?: it
                    }

                    _charactersMutableStateFlow.value = resultedList

                    currentPage++
                }
                is ApiResult.Error -> {
                    val errorMessage = result.message
                    val throwable = result.throwable
                    Log.e(
                        "CharacterViewModel",
                        "Error getting filtered characters: $errorMessage",
                        throwable
                    )
                }
            }
        }
    }

    fun setFilter(filter: FilterCharacters): FilterCharacters {
        isFilterApplied = true
        currentPage = 1
        filterCharacters = FilterCharacters(
            name = filter.name,
            species = filter.species,
            status = filter.status,
            gender = filter.gender
        )
        getFilteredCharacters(filterCharacters.name, filterCharacters.status.toString(), filterCharacters.gender.toString(), filterCharacters.species)

        return filterCharacters
    }

    fun cancelFilter(){
        currentPage = 1
        getAllCharacters()
    }

    fun onFavoriteStateChanged(list: List<ResultsCharacter>){
        viewModelScope.launch {
            favoriteList = list
            val currentList = updatedList.toList()
            val resultedList = currentList.map {
                list.firstOrNull { character ->
                    character.id == it.id
                } ?: it
            }
            _charactersMutableStateFlow.value = resultedList
        }
    }
}