package com.example.retrofitapp.presentation.location

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.retrofitapp.domain.model.character.ResultsCharacter
import com.example.retrofitapp.domain.model.location.ResultsLocation
import com.example.retrofitapp.data.repository.ApiResult
import com.example.retrofitapp.data.repository.RickAndMortyRepository
import kotlinx.coroutines.launch

class LocationDetailViewModel(id: Int) : ViewModel() {

    private val rickAndMortyRepository = RickAndMortyRepository

    private val _characterMutableLiveData: MutableLiveData<List<ResultsCharacter>> = MutableLiveData()
    val characterMutableLiveData: LiveData<List<ResultsCharacter>> = _characterMutableLiveData

    private val _locations: MutableLiveData<ResultsLocation> = MutableLiveData()
    val locations: LiveData<ResultsLocation> = _locations

    init {
        getLocationInfo(id)
    }

    private fun getLocationInfo(id: Int){
        viewModelScope.launch{
            when (val result = rickAndMortyRepository.getLocationInfo(id)) {
                is ApiResult.Success -> {
                    this@LocationDetailViewModel._locations.value = result.value
                    getMultipleCharacters(result.value.residents)
                }
                is ApiResult.Error -> {
                    val errorMessage = result.message
                    val throwable = result.throwable
                    Log.e(
                        "LocationDetailViewModel",
                        "Error getting location info: $errorMessage",
                        throwable
                    )
                }
            }
        }
    }

    private fun getMultipleCharacters(urls: List<String>) {
        if (urls.isNotEmpty()) {
            val lastElements = urls.map { it.substring(it.lastIndexOf("/") + 1) }
            val ids = lastElements.joinToString(separator = ",")

            viewModelScope.launch{
                when (val result = rickAndMortyRepository.getMultipleCharacters(ids)) {
                    is ApiResult.Success -> {
                        this@LocationDetailViewModel._characterMutableLiveData.value = result.value
                    }
                    is ApiResult.Error -> {
                        val errorMessage = result.message
                        val throwable = result.throwable
                        Log.e(
                            "LocationDetailViewModel",
                            "Error getting characters of location: $errorMessage",
                            throwable
                        )
                    }
                }
            }
        }
    }

}