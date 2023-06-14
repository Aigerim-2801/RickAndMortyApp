package com.example.retrofitapp.presentation.location

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.retrofitapp.domain.model.location.ResultsLocation
import com.example.retrofitapp.data.repository.ApiResult
import com.example.retrofitapp.data.repository.RickAndMortyRepository
import kotlinx.coroutines.launch


class LocationViewModel : ViewModel() {

    private var currentPage = 1
    private val rickAndMortyRepository = RickAndMortyRepository

    private val _locationsMutableLiveData = MutableLiveData<List<ResultsLocation>>()
    val locationsMutableLiveData: LiveData<List<ResultsLocation>> = _locationsMutableLiveData
    private var listOfItem = mutableListOf<ResultsLocation>()

    init {
        getAllLocations()
    }

    fun getAllLocations() {
        viewModelScope.launch{
            when (val result = rickAndMortyRepository.getAllLocations(currentPage)) {
                is ApiResult.Success -> {
                    listOfItem = listOfItem.toMutableList()
                    listOfItem.addAll(result.value.results)
                    _locationsMutableLiveData.value = listOfItem
                    currentPage++
                }
                is ApiResult.Error -> {
                    val errorMessage = result.message
                    val throwable = result.throwable
                    Log.e(
                        "LocationViewModel",
                        "Error getting all locations: $errorMessage",
                        throwable
                    )
                }
            }
        }
    }
}