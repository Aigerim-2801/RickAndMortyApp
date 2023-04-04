package com.example.retrofitapp.sources.episode.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.retrofitapp.sources.character.data.ResultsCharacter
import com.example.retrofitapp.sources.episode.data.ResultsEpisode
import com.example.retrofitapp.sources.repository.ApiResult
import com.example.retrofitapp.sources.repository.RickAndMortyRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
                    result.value.characters.let { getMultipleCharacters(it) }
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

    private fun getMultipleCharacters(urls:List<String>) {
        if (urls.isNotEmpty()) {
            val ids = urls.map { it.substring(it.lastIndexOf("/") + 1) }
            val last = ids.joinToString(separator = ",")
            val call = rickAndMortyRepository.getMultipleCharacters(last)
            call.enqueue(object : Callback<List<ResultsCharacter>> {
                override fun onResponse(
                    call: Call<List<ResultsCharacter>>,
                    response: Response<List<ResultsCharacter>>
                ) {
                    if (response.isSuccessful) {
                        _characterMutableLiveData.value = response.body()
                    }
                }
                override fun onFailure(call: Call<List<ResultsCharacter>>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        }
    }
}