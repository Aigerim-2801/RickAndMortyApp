package com.example.retrofitapp.presentation.character

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.retrofitapp.data.remote.CharactersDao
import com.example.retrofitapp.domain.model.character.FilterCharacters
import com.example.retrofitapp.domain.model.character.ResultsCharacter
import com.example.retrofitapp.data.repository.ApiResult
import com.example.retrofitapp.data.repository.RickAndMortyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CharacterViewModel : ViewModel() {

    private var currentPage = 1
    private val rickAndMortyRepository = RickAndMortyRepository

    private var isFilterApplied: Boolean = false

    var filterCharacters = FilterCharacters()

    private val _charactersMutableStateFlow = MutableStateFlow<List<ResultsCharacter>>(emptyList())
    val charactersStateFlow: StateFlow<List<ResultsCharacter>> = _charactersMutableStateFlow
    private val updatedList = _charactersMutableStateFlow.value.toMutableList()

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
                      _charactersMutableStateFlow.value = updatedList
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
                    updatedList.addAll(result.value.results)
                    _charactersMutableStateFlow.value = updatedList
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


    fun deleteCharacter(character: ResultsCharacter): Int {
        val position = updatedList.indexOf(character)
        updatedList.remove(character)
        _charactersMutableStateFlow.value = updatedList
        return position
    }

    fun undo(position: Int, character: ResultsCharacter) {
        updatedList.add(position, character)
        _charactersMutableStateFlow.value = updatedList
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


    fun updateFavoriteCharacters(dao: CharactersDao) {
        updatedList.clear()
        updatedList.addAll(dao.getAll())
        _charactersMutableStateFlow.value = updatedList
    }

    fun checkFlag(isFavorite: Boolean, dao: CharactersDao, character: ResultsCharacter){
        if(!isFavorite){
            character.isFavorite = true
            dao.insert(character)
        }else{
            character.isFavorite = false
            dao.delete(character)
        }
//        updateFavoriteCharacters(dao)
    }
}