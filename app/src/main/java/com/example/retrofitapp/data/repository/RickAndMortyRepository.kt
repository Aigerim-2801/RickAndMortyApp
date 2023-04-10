package com.example.retrofitapp.data.repository

import com.example.retrofitapp.data.utils.RetrofitInstance
import com.example.retrofitapp.domain.model.character.Characters
import com.example.retrofitapp.domain.model.character.ResultsCharacter
import com.example.retrofitapp.domain.model.episode.Episodes
import com.example.retrofitapp.domain.model.episode.ResultsEpisode
import com.example.retrofitapp.domain.model.location.Locations
import com.example.retrofitapp.domain.model.location.ResultsLocation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object RickAndMortyRepository {

    private val rickAndMortyApi = RetrofitInstance.getRickAndMortyApi()

    fun getAllCharacters(page: Int, callback: (ApiResult<Characters>) -> Unit) {
        val call = rickAndMortyApi.getAllCharacters(page)
        call.enqueue(object : Callback<Characters> {
            override fun onResponse(
                call: Call<Characters>,
                response: Response<Characters>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        callback(ApiResult.Success(data))
                    }
                } else {
                    callback(ApiResult.Error(response.message(), Throwable()))
                }
            }

            override fun onFailure(call: Call<Characters>, t: Throwable) {
                callback(ApiResult.Error(t.message, t))
            }
        })
    }


    fun getCharacterInfo(id: Int, callback: (ApiResult<ResultsCharacter>) -> Unit) {
        val call = rickAndMortyApi.getCharacterInfo(id)
        call.enqueue(object : Callback<ResultsCharacter> {
            override fun onResponse(
                call: Call<ResultsCharacter>,
                response: Response<ResultsCharacter>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        callback(ApiResult.Success(data))
                    }
                } else {
                    callback(ApiResult.Error(response.message(), Throwable()))
                }
            }

            override fun onFailure(call: Call<ResultsCharacter>, t: Throwable) {
                callback(ApiResult.Error(t.message, t))
            }
        })
    }

    fun getMultipleCharacters(ids: String, callback: (ApiResult<List<ResultsCharacter>>) -> Unit) {
        val call = rickAndMortyApi.getMultipleCharacters(ids)
        call.enqueue(object : Callback<List<ResultsCharacter>> {
            override fun onResponse(
                call: Call<List<ResultsCharacter>>,
                response: Response<List<ResultsCharacter>>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        callback(ApiResult.Success(data))
                    }
                } else {
                    callback(ApiResult.Error(response.message(), Throwable()))
                }
            }
            override fun onFailure(call: Call<List<ResultsCharacter>>, t: Throwable) {
                callback(ApiResult.Error(t.message, t))
            }
        })
    }

    fun getFilteredCharacters(name: String, status: String, gender: String, species: String, page: Int,  callback: (ApiResult<Characters>) -> Unit) {
        val call = rickAndMortyApi.getFilteredCharacters(name, status, gender, species, page)
        call.enqueue(object : Callback<Characters> {
            override fun onResponse(
                call: Call<Characters>,
                response: Response<Characters>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        callback(ApiResult.Success(data))
                    }
                } else {
                    callback(
                        ApiResult.Error(
                            "Error getting filtered characters. Response code: ${response.code()}, message: ${response.message()}",
                            Throwable()
                        )
                    )
                }
            }

            override fun onFailure(call: Call<Characters>, t: Throwable) {
                callback(ApiResult.Error(t.message, t))
            }
        })
    }

    fun getAllEpisodes(page: Int, callback: (ApiResult<Episodes>) -> Unit) {
        val call = rickAndMortyApi.getAllEpisodes(page)
        call.enqueue(object : Callback<Episodes> {
            override fun onResponse(
                call: Call<Episodes>,
                response: Response<Episodes>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        callback(ApiResult.Success(data))
                    }
                } else {
                    callback(ApiResult.Error(response.message(), Throwable()))
                }
            }
            override fun onFailure(call: Call<Episodes>, t: Throwable) {
                callback(ApiResult.Error(t.message, t))
            }
        })
    }

    fun getEpisodeInfo(id: Int, callback: (ApiResult<ResultsEpisode>) -> Unit) {
        val call = rickAndMortyApi.getEpisodeInfo(id)
        call.enqueue(object : Callback<ResultsEpisode> {
            override fun onResponse(
                call: Call<ResultsEpisode>,
                response: Response<ResultsEpisode>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        callback(ApiResult.Success(data))
                    }
                } else {
                    callback(ApiResult.Error(response.message(), Throwable()))
                }
            }
            override fun onFailure(call: Call<ResultsEpisode>, t: Throwable) {
                callback(ApiResult.Error(t.message, t))
            }
        })
    }

    fun getMultipleEpisodes(ids: String, callback: (ApiResult<List<ResultsEpisode>>) -> Unit) {
        val call = rickAndMortyApi.getMultipleEpisodes(ids)
        call.enqueue(object : Callback<List<ResultsEpisode>> {
            override fun onResponse(
                call: Call<List<ResultsEpisode>>,
                response: Response<List<ResultsEpisode>>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        callback(ApiResult.Success(data))
                    }
                } else {
                    callback(ApiResult.Error(response.message(), Throwable()))
                }
            }
            override fun onFailure(call: Call<List<ResultsEpisode>>, t: Throwable) {
                callback(ApiResult.Error(t.message, t))
            }
        })
    }

    fun getAllLocations(page: Int, callback: (ApiResult<Locations>) -> Unit) {
        val call = rickAndMortyApi.getAllLocations(page)
        call.enqueue(object : Callback<Locations> {
            override fun onResponse(
                call: Call<Locations>,
                response: Response<Locations>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        callback(ApiResult.Success(data))
                    }
                } else {
                    callback(ApiResult.Error(response.message(), Throwable()))
                }
            }
            override fun onFailure(call: Call<Locations>, t: Throwable) {
                callback(ApiResult.Error(t.message, t))
            }
        })
    }

    fun getLocationInfo(id: Int, callback: (ApiResult<ResultsLocation>) -> Unit) {
        val call = rickAndMortyApi.getLocationInfo(id)
        call.enqueue(object : Callback<ResultsLocation> {
            override fun onResponse(
                call: Call<ResultsLocation>,
                response: Response<ResultsLocation>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        callback(ApiResult.Success(data))
                    }
                } else {
                    callback(ApiResult.Error(response.message(), Throwable()))
                }
            }
            override fun onFailure(call: Call<ResultsLocation>, t: Throwable) {
                callback(ApiResult.Error(t.message, t))
            }
        })
    }
}