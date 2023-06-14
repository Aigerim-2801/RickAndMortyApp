package com.example.retrofitapp.presentation.character

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.retrofitapp.domain.model.character.ResultsCharacter
import com.example.retrofitapp.domain.model.character.Status
import com.example.retrofitapp.data.repository.RickAndMortyRepository
import com.example.retrofitapp.domain.model.episode.ResultsEpisode
import com.example.retrofitapp.data.repository.ApiResult
import kotlinx.coroutines.launch

class CharacterDetailViewModel(id: Int) : ViewModel() {

    private val rickAndMortyRepository = RickAndMortyRepository

    private val _characterInfoLiveData = MutableLiveData<ResultsCharacter>()
    val characterInfoLiveData: LiveData<ResultsCharacter> = _characterInfoLiveData

    private val _episodes = MutableLiveData<ApiResult<List<ResultsEpisode>>>()
    val episodes: LiveData<ApiResult<List<ResultsEpisode>>> = _episodes

    private val _isEpisodeLoading = MutableLiveData(false)
    val isEpisodeLoading: LiveData<Boolean> = _isEpisodeLoading


    init {
        getCharacterInfo(id)
    }

    fun getIdUrl(url: String): String {
        return url.substring(url.lastIndexOf("/") + 1)
    }

    private fun getCharacterInfo(id: Int) {
        viewModelScope.launch {
            when (val result = rickAndMortyRepository.getCharacterInfo(id)) {
                is ApiResult.Success -> {
                    _characterInfoLiveData.value = result.value
                    getMultipleEpisodes(result.value.episode)
                }
                is ApiResult.Error -> {
                    val errorMessage = result.message
                    val throwable = result.throwable
                    Log.e(
                        "CharacterDetailViewModel",
                        "Error getting character info: $errorMessage",
                        throwable
                    )
                }
            }
        }
    }

    private fun getMultipleEpisodes(urls: List<String>) {
        _isEpisodeLoading.value = true
        if (urls.isNotEmpty() && urls.size > 1) {
            val lastElements = urls.map { it.substring(it.lastIndexOf("/") + 1) }
            val ids = lastElements.joinToString(separator = ",")

            viewModelScope.launch {
                when (val result = rickAndMortyRepository.getMultipleEpisodes(ids)) {
                    is ApiResult.Success -> {
                        _episodes.value = result
                    }
                    is ApiResult.Error -> {
                        val errorMessage = result.message
                        val throwable = result.throwable
                        Log.e(
                            "CharacterDetailViewModel",
                            "Error getting episodes of character info: $errorMessage",
                            throwable
                        )
                    }
                }
                _isEpisodeLoading.value = false
            }
        } else if (urls.size == 1) {
            val id = urls[0].substring(urls[0].lastIndexOf("/") + 1).toInt()

            viewModelScope.launch {
                when (val result = rickAndMortyRepository.getEpisodeInfo(id)) {
                    is ApiResult.Success -> {
                        _episodes.value = ApiResult.Success(listOf(result.value))
                    }
                    is ApiResult.Error -> {
                        val errorMessage = result.message
                        val throwable = result.throwable
                        _episodes.value = ApiResult.Error(errorMessage, throwable)
                    }
                }
                _isEpisodeLoading.value = false
            }
        }
    }

    fun typeCharacter(type: String): String {
        return type.ifEmpty {
            Status.Unknown.toString()
        }
    }

}