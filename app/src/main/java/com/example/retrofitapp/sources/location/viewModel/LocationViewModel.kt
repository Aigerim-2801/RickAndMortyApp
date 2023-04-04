package com.example.retrofitapp.sources.location.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.retrofitapp.sources.location.data.Location
import com.example.retrofitapp.sources.location.data.ResultsLocation
import com.example.retrofitapp.sources.repository.RickAndMortyRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LocationViewModel : ViewModel() {

    private var currentPage = 1
    private val rickAndMortyRepository = RickAndMortyRepository

    private val _locations = MutableLiveData<List<ResultsLocation>>()
    val locations: LiveData<List<ResultsLocation>> = _locations

    init {
        getAllLocations()
    }

    fun getAllLocations(){
        val call = rickAndMortyRepository.getAllLocations(currentPage)
        currentPage++
        call.enqueue(object : Callback<Location> {
            override fun onResponse(
                call: Call<Location>,
                response: Response<Location>
            ) {
                if (response.isSuccessful) {
                    _locations.value = response.body()?.results
                }
            }
            override fun onFailure(call: Call<Location>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}