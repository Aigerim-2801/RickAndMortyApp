package com.example.retrofitapp.sources.episode.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.retrofitapp.sources.episode.data.Episode
import com.example.retrofitapp.sources.episode.data.ResultsEpisode
import com.example.retrofitapp.sources.repository.RickAndMortyRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EpisodeViewModel : ViewModel() {

    private val rickAndMortyRepository = RickAndMortyRepository

    private val _episodes = MutableLiveData<List<ResultsEpisode>>()
    val episodes: LiveData<List<ResultsEpisode>> = _episodes

    init {
        getAllEpisodes()
    }

    private fun getAllEpisodes(){
        val call = rickAndMortyRepository.getAllEpisodes()
        call.enqueue(object : Callback<Episode> {
            override fun onResponse(
                call: Call<Episode>,
                response: Response<Episode>
            ) {
                if (response.isSuccessful) {
                    _episodes.value = response.body()?.results
                }
            }
            override fun onFailure(call: Call<Episode>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}