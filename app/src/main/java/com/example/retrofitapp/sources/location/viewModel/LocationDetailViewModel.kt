package com.example.retrofitapp.sources.location.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.retrofitapp.sources.character.data.ResultsCharacter
import com.example.retrofitapp.sources.location.data.ResultsLocation
import com.example.retrofitapp.sources.repository.RickAndMortyRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LocationDetailViewModel(id: Int) : ViewModel() {

    private val rickAndMortyRepository = RickAndMortyRepository

    private val _characterMutableLiveData = MutableLiveData<List<ResultsCharacter>>()
    val characterMutableLiveData: LiveData<List<ResultsCharacter>> = _characterMutableLiveData

    private val _locations = MutableLiveData<ResultsLocation>()
    val locations: LiveData<ResultsLocation> = _locations

    private val listOfItem = mutableListOf<ResultsCharacter>()

    init {
        getLocationInfo(id)
    }

    private fun getLocationInfo(id: Int){
        val call = rickAndMortyRepository.getLocationInfo(id)
        call.enqueue(object : Callback<ResultsLocation> {
            override fun onResponse(
                call: Call<ResultsLocation>,
                response: Response<ResultsLocation>
            ) {
                if (response.isSuccessful) {
                    _locations.value = response.body()
                    response.body()?.residents?.let { getMultipleCharacters(it) }
                }
            }
            override fun onFailure(call: Call<ResultsLocation>, t: Throwable) {
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

    fun deleteCharacter(position: Int){
        listOfItem.removeAt(position)
    }
}