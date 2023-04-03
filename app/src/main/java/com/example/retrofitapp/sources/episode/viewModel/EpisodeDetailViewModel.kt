package com.example.retrofitapp.sources.episode.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.retrofitapp.sources.character.data.ResultsCharacter
import com.example.retrofitapp.sources.episode.data.ResultsEpisode
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
        val call = rickAndMortyRepository.getEpisodeInfo(id)
        call.enqueue(object : Callback<ResultsEpisode> {
            override fun onResponse(
                call: Call<ResultsEpisode>,
                response: Response<ResultsEpisode>
            ) {
                if (response.isSuccessful) {
                    _episodes.value = response.body()
                    response.body()?.characters?.let { getMultipleCharacters(it) }
                }
            }
            override fun onFailure(call: Call<ResultsEpisode>, t: Throwable) {
                t.printStackTrace()
            }
        })
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