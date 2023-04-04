package com.example.retrofitapp.sources.character.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.retrofitapp.sources.character.data.Characters
import com.example.retrofitapp.sources.character.data.ResultsCharacter
import com.example.retrofitapp.sources.repository.RickAndMortyRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CharacterViewModel : ViewModel() {

    private var currentPage = 1
    private val rickAndMortyRepository = RickAndMortyRepository

    private val _characterMutableLiveData = MutableLiveData<List<ResultsCharacter>>()
    val characterMutableLiveData: LiveData<List<ResultsCharacter>> = _characterMutableLiveData
    private val listOfItem = mutableListOf<ResultsCharacter>()


    init {
        getAllCharacters()
    }

    fun getAllCharacters() {
        val call = rickAndMortyRepository.getAllCharacters(currentPage)
        currentPage++
        call.enqueue(object : Callback<Characters> {
            override fun onResponse(
                call: Call<Characters>,
                response: Response<Characters>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()?.results
                    if (result != null) {
                        listOfItem.addAll(result)
                        _characterMutableLiveData.value = listOfItem
                    }
                }
            }
            override fun onFailure(call: Call<Characters>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    fun getFilteredCharacter(
        name: String,
        status: String,
        gender: String,
        species: String
    ) {
        val call = rickAndMortyRepository.getFilteredCharacter(name, status, gender, species)
        call.enqueue(object : Callback<Characters> {
            override fun onResponse(
                call: Call<Characters>,
                response: Response<Characters>
            ) {
                if (response.isSuccessful) {
                    _characterMutableLiveData.value = response.body()?.results

                }
            }

            override fun onFailure(call: Call<Characters>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

}