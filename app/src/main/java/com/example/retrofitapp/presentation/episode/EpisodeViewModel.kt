package com.example.retrofitapp.presentation.episode

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.retrofitapp.domain.model.episode.ResultsEpisode
import com.example.retrofitapp.data.repository.ApiResult
import com.example.retrofitapp.data.repository.RickAndMortyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EpisodeViewModel @Inject constructor(private val rickAndMortyRepository: RickAndMortyRepository): ViewModel() {

    private var currentPage = 1

    private val _episodesMutableStateFlow = MutableStateFlow<List<ResultsEpisode>>(emptyList())
    val episodesStateFlow: StateFlow<List<ResultsEpisode>> = _episodesMutableStateFlow

    init {
        getAllEpisodes()
    }

    fun getAllEpisodes() {
        viewModelScope.launch {
            when (val result = rickAndMortyRepository.getAllEpisodes(currentPage)) {
                is ApiResult.Success -> {
                    val updatedList = _episodesMutableStateFlow.value.toMutableList()
                    updatedList.addAll(result.value.results)
                    _episodesMutableStateFlow.value = updatedList
                    currentPage++
                }
                is ApiResult.Error -> {
                    val errorMessage = result.message
                    val throwable = result.throwable
                    Log.e(
                        "EpisodeViewModel",
                        "Error getting all episodes: $errorMessage",
                        throwable
                    )
                }
            }
        }
    }
}