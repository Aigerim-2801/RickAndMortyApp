package com.example.retrofitapp.sources.episode.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.retrofitapp.sources.episode.data.Episodes
import com.example.retrofitapp.sources.episode.data.ResultsEpisode
import com.example.retrofitapp.sources.repository.RickAndMortyRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EpisodeViewModel : ViewModel() {

    private var currentPage = 1
    private val rickAndMortyRepository = RickAndMortyRepository

    private val _episodes = MutableLiveData<List<ResultsEpisode>>()
    val episodes: LiveData<List<ResultsEpisode>> = _episodes

    init {
        getAllEpisodes()
    }

    fun getAllEpisodes(){
        val call = rickAndMortyRepository.getAllEpisodes(currentPage)
        currentPage++
        call.enqueue(object : Callback<Episodes> {
            override fun onResponse(
                call: Call<Episodes>,
                response: Response<Episodes>
            ) {
                if (response.isSuccessful) {
                    _episodes.value = response.body()?.results
                }
            }
            override fun onFailure(call: Call<Episodes>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}