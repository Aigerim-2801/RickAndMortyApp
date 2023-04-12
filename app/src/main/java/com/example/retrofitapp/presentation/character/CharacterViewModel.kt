package com.example.retrofitapp.presentation.character

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.retrofitapp.domain.model.character.FilterCharacters
import com.example.retrofitapp.domain.model.character.ResultsCharacter
import com.example.retrofitapp.data.repository.ApiResult
import com.example.retrofitapp.data.repository.RickAndMortyRepository
import com.example.retrofitapp.data.utils.DBHelper

class CharacterViewModel : ViewModel() {

    private var currentPage = 1
    private val rickAndMortyRepository = RickAndMortyRepository

    private var isFilterApplied: Boolean = false

    var filterCharacters = FilterCharacters()

    private val _characterMutableLiveData = MutableLiveData<List<ResultsCharacter>>()
    val characterMutableLiveData: LiveData<List<ResultsCharacter>> = _characterMutableLiveData
    private var listOfItem = mutableListOf<ResultsCharacter>()

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
        rickAndMortyRepository.getAllCharacters(currentPage) { result ->
            when (result) {
                is ApiResult.Success -> {
                    if (currentPage == 1) {
                        listOfItem.clear()
                    }
                    listOfItem.addAll(result.value.results)
                    _characterMutableLiveData.value = listOfItem
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
        rickAndMortyRepository.getFilteredCharacters(name, status, gender, species, currentPage) { result ->
            when (result) {
                is ApiResult.Success -> {
                    if (currentPage == 1) {
                        listOfItem.clear()
                    }
                    listOfItem.addAll(result.value.results)
                    _characterMutableLiveData.value = listOfItem
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
        val position = listOfItem.indexOf(character)
        listOfItem = listOfItem.toMutableList()
        listOfItem.remove(character)
        _characterMutableLiveData.value = listOfItem
        return position
    }

    fun undo(position: Int, character: ResultsCharacter) {
        listOfItem = listOfItem.toMutableList()
        listOfItem.add(position, character)
        _characterMutableLiveData.value = listOfItem
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


    fun updateFavoriteCharacters(dbHelper: DBHelper) {
        listOfItem.addAll(rickAndMortyRepository.getAllFavorite(dbHelper))
        _characterMutableLiveData.value = listOfItem
    }

    fun checkFlag(isFavorite: Boolean, dbHelper: DBHelper, resultsCharacter: ResultsCharacter){
        if(!isFavorite){
            rickAndMortyRepository.save(dbHelper, resultsCharacter)
        }else{
            rickAndMortyRepository.remove(dbHelper, resultsCharacter)
        }
        listOfItem.addAll(rickAndMortyRepository.getAllFavorite(dbHelper))
        _characterMutableLiveData.value = listOfItem
    }
}