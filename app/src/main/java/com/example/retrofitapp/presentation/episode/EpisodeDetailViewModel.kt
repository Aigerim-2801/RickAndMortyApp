package com.example.retrofitapp.presentation.episode

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.retrofitapp.domain.model.character.ResultsCharacter
import com.example.retrofitapp.domain.model.episode.ResultsEpisode
import com.example.retrofitapp.data.repository.ApiResult
import com.example.retrofitapp.data.repository.RickAndMortyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EpisodeDetailViewModel @Inject constructor(id: Int, private val rickAndMortyRepository: RickAndMortyRepository) : ViewModel() {

    private val _characterMutableLiveData = MutableLiveData<List<ResultsCharacter>>()
    val characterMutableLiveData: LiveData<List<ResultsCharacter>> = _characterMutableLiveData

    private val _episodes = MutableLiveData<ResultsEpisode>()
    val episodes: LiveData<ResultsEpisode> = _episodes

    init {
        getEpisodeInfo(id)
    }

    private fun getEpisodeInfo(id: Int){
        viewModelScope.launch {
            when (val result = rickAndMortyRepository.getEpisodeInfo(id)) {
                is ApiResult.Success -> {
                    this@EpisodeDetailViewModel._episodes.value = result.value
                    getMultipleCharacters(result.value.characters)
                }
                is ApiResult.Error -> {
                    val errorMessage = result.message
                    val throwable = result.throwable
                    Log.e(
                        "EpisodeDetailViewModel",
                        "Error getting episode info: $errorMessage",
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

            viewModelScope.launch {
                when (val result = rickAndMortyRepository.getMultipleCharacters(ids)) {
                    is ApiResult.Success -> {
                        this@EpisodeDetailViewModel._characterMutableLiveData.value = result.value
                    }
                    is ApiResult.Error -> {
                        val errorMessage = result.message
                        val throwable = result.throwable
                        Log.e(
                            "EpisodeDetailViewModel",
                            "Error getting characters of episode: $errorMessage",
                            throwable
                        )
                    }
                }
            }
        }
    }
}