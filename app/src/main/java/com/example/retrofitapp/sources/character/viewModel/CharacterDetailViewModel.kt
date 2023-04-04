package com.example.retrofitapp.sources.character.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.retrofitapp.sources.character.data.ResultsCharacter
import com.example.retrofitapp.sources.character.data.Status
import com.example.retrofitapp.sources.repository.RickAndMortyRepository
import com.example.retrofitapp.sources.episode.data.ResultsEpisode
import com.example.retrofitapp.sources.repository.ApiResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
        val call = rickAndMortyRepository.getCharacterInfo(id)
        call.enqueue(object : Callback<ResultsCharacter> {
            override fun onResponse(
                call: Call<ResultsCharacter>,
                response: Response<ResultsCharacter>
            ) {
                if (response.isSuccessful) {
                    _characterInfoLiveData.value = response.body()
                    response.body()?.episode?.let { getEpisodes(it) }
                }
            }

            override fun onFailure(call: Call<ResultsCharacter>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

//    private fun getEpisodes(urls:List<String>) {
//        if (urls.isNotEmpty()) {
//            val lastElements = urls.map { it.substring(it.lastIndexOf("/") + 1) }
//            val ids = lastElements.joinToString(separator = ",")
//            val call = rickAndMortyRepository.getMultipleEpisodes(ids)
//            call.enqueue(object : Callback<List<ResultsEpisode>> {
//                override fun onResponse(
//                    call: Call<List<ResultsEpisode>>,
//                    response: Response<List<ResultsEpisode>>
//                ) {
//                    if (response.isSuccessful) {
//                        _episodes.value = response.body()
//                    }
//                }
//                override fun onFailure(call: Call<List<ResultsEpisode>>, t: Throwable) {
//                    t.printStackTrace()
//                }
//            })
//        }
//    }


    private fun getEpisodes(urls: List<String>) {
        _isEpisodeLoading.value = true
        if (urls.isNotEmpty() && urls.size > 1) {
            val lastElements = urls.map { it.substring(it.lastIndexOf("/") + 1) }
            val ids = lastElements.joinToString(separator = ",")
            rickAndMortyRepository.getMultipleEpisode(ids) { result ->
                when (result) {
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
            rickAndMortyRepository.getEpisodeInfo(id) { result ->
                when (result) {
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

//    private fun getEpisodes(urls:List<String>) {
//        if (urls.isNotEmpty()) {
//            val lastElements = urls.map { it.substring(it.lastIndexOf("/") + 1) }
//            val ids = lastElements.joinToString(separator = ",")
//            viewModelScope.launch {
//                when (val call = rickAndMortyRepository.getMultipleEpisode(ids)) {
//                    is ApiResult.Success ->  _episodes.postValue(ApiResult.Success(call.value))
//                    is ApiResult.Error -> {
//                        val errorMessage = call.message
//                        val throwable = call.throwable
//                        Log.e(
//                            "CharacterDetailViewModel",
//                            "Error getting character info: $errorMessage",
//                            throwable
//                        )
//                    }
//                }
//            }
//        }
//    }

    fun typeCharacter(type: String): String {
        return type.ifEmpty {
            Status.Unknown.toString()
        }
    }

}