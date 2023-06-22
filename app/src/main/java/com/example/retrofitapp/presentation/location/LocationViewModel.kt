package com.example.retrofitapp.presentation.location

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.retrofitapp.domain.model.location.ResultsLocation
import com.example.retrofitapp.data.repository.ApiResult
import com.example.retrofitapp.data.repository.RickAndMortyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LocationViewModel @Inject constructor(private val rickAndMortyRepository: RickAndMortyRepository) : ViewModel() {

    private var currentPage = 1

    private val _locationsMutableStateFlow = MutableStateFlow<List<ResultsLocation>>(emptyList())
    val locationsStateFlow: StateFlow<List<ResultsLocation>> = _locationsMutableStateFlow

    init {
        getAllLocations()
    }

    fun getAllLocations() {
        viewModelScope.launch{
            when (val result = rickAndMortyRepository.getAllLocations(currentPage)) {
                is ApiResult.Success -> {
                    val updatedList = _locationsMutableStateFlow.value.toMutableList()
                    updatedList.addAll(result.value.results)
                    _locationsMutableStateFlow.value = updatedList
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