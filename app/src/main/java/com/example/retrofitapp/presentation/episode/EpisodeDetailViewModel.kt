package com.example.retrofitapp.presentation.episode

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.retrofitapp.domain.model.character.ResultsCharacter
import com.example.retrofitapp.domain.model.episode.ResultsEpisode
import com.example.retrofitapp.data.repository.ApiResult
import com.example.retrofitapp.data.repository.RickAndMortyRepository

class EpisodeDetailViewModel(id: Int) : ViewModel() {

    private val rickAndMortyRepository = RickAndMortyRepository

    private val _characterMutableLiveData = MutableLiveData<List<ResultsCharacter>>()
    val characterMutableLiveData: LiveData<List<ResultsCharacter>> = _characterMutableLiveData

    private val _episodes = MutableLiveData<ResultsEpisode>()
    val episodes: LiveData<ResultsEpisode> = _episodes

    init {
        getEpisodeInfo(id)
    }

    private fun getEpisodeInfo(id: Int){
        rickAndMortyRepository.getEpisodeInfo(id){ result ->
            when (result) {
                is ApiResult.Success -> {
                    _episodes.value = result.value
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
            rickAndMortyRepository.getMultipleCharacters(ids) { result ->
                when (result) {
                    is ApiResult.Success -> {
                        _characterMutableLiveData.value = result.value
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