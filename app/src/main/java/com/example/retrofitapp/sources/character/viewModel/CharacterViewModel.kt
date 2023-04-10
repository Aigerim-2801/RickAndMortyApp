package com.example.retrofitapp.sources.character.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.retrofitapp.sources.character.data.FilterCharacter
import com.example.retrofitapp.sources.character.data.ResultsCharacter
import com.example.retrofitapp.sources.repository.ApiResult
import com.example.retrofitapp.sources.repository.RickAndMortyRepository

class CharacterViewModel : ViewModel() {

    private var currentPage = 1
    private val rickAndMortyRepository = RickAndMortyRepository

    private var isFilterApplied: Boolean = false

     var filterCharacter = FilterCharacter()

    private val _characterMutableLiveData = MutableLiveData<List<ResultsCharacter>>()
    val characterMutableLiveData: LiveData<List<ResultsCharacter>> = _characterMutableLiveData
    private var listOfItem = mutableListOf<ResultsCharacter>()

    init {
        getAllCharacters()
    }

//    fun getAllCharacters() {
//        val call = rickAndMortyRepository.getAllCharacters(currentPage)
//        currentPage++
//        call.enqueue(object : Callback<Characters> {
//            override fun onResponse(
//                call: Call<Characters>,
//                response: Response<Characters>
//            ) {
//                if (response.isSuccessful) {
//                    val result = response.body()?.results
//                    if (result != null) {
//                        listOfItem.addAll(result)
//                        _characterMutableLiveData.value = listOfItem
//                    }
//                }
//            }
//            override fun onFailure(call: Call<Characters>, t: Throwable) {
//                t.printStackTrace()
//            }
//        })
//    }


    fun getCharacters(){
        if (!isFilterApplied) {
            getAllCharacters()
        } else {
            getFilteredCharacter(
                name = filterCharacter.name,
                status = filterCharacter.status.toString(),
                gender = filterCharacter.gender.toString(),
                species = filterCharacter.species
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

    private fun getFilteredCharacter(name: String, status: String, gender: String, species: String) {
        rickAndMortyRepository.getFilteredCharacter(name, status, gender, species, currentPage) { result ->
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

    fun setFilter(filter: FilterCharacter): FilterCharacter {
        isFilterApplied = true
        currentPage = 1
        filterCharacter = FilterCharacter(
            name = filter.name,
            species = filter.species,
            status = filter.status,
            gender = filter.gender
        )
        getFilteredCharacter(filterCharacter.name, filterCharacter.status.toString(), filterCharacter.gender.toString(), filterCharacter.species)

        return filterCharacter
    }

    fun cancelFilter(){
        currentPage = 1
        getAllCharacters()
    }
}