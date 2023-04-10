package com.example.retrofitapp.sources.repository

import com.example.retrofitapp.sources.RetrofitInstance
import com.example.retrofitapp.sources.character.data.Characters
import com.example.retrofitapp.sources.character.data.ResultsCharacter
import com.example.retrofitapp.sources.episode.data.Episodes
import com.example.retrofitapp.sources.episode.data.ResultsEpisode
import com.example.retrofitapp.sources.location.data.Locations
import com.example.retrofitapp.sources.location.data.ResultsLocation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object RickAndMortyRepository {

    private val rickAndMortyApi = RetrofitInstance.getRickAndMortyApi()

//    fun getAllCharacters(page: Int) = rickAndMortyApi.getAllCharacters(page)

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

//    fun getFilteredCharacter(name: String, status: String, gender: String, species: String) = rickAndMortyApi.getFilteredCharacter(name, status, gender, species)
    fun getCharacterInfo(id: Int) = rickAndMortyApi.getCharacterInfo(id)

    fun getFilteredCharacter(name: String, status: String, gender: String, species: String, page: Int,  callback: (ApiResult<Characters>) -> Unit) {
        val call = rickAndMortyApi.getFilteredCharacter(name, status, gender, species, page)
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
                    callback(ApiResult.Error("Error getting filtered characters. Response code: ${response.code()}, message: ${response.message()}", Throwable()))

                }
            }

            override fun onFailure(call: Call<Characters>, t: Throwable) {
                callback(ApiResult.Error(t.message, t))
            }
        })
    }

//    fun getCharacterInfo(id: Int, callback: (ApiResult<ResultsCharacter>) -> Unit) {
//        val call = rickAndMortyApi.getCharacterInfo(id)
//        call.enqueue(object : Callback<ResultsCharacter> {
//            override fun onResponse(
//                call: Call<ResultsCharacter>,
//                response: Response<ResultsCharacter>
//            ) {
//                if (response.isSuccessful) {
//                    val data = response.body()
//                    if (data != null) {
//                        callback(ApiResult.Success(data))
//                    }
//                } else {
//                    callback(ApiResult.Error(response.message(), Throwable()))
//                }
//            }
//
//            override fun onFailure(call: Call<ResultsCharacter>, t: Throwable) {
//                callback(ApiResult.Error(t.message, t))
//            }
//        })
//    }

//    fun getMultipleEpisodes(ids: String): Call<List<ResultsEpisode>> = rickAndMortyApi.getMultipleEpisodes(ids)

    fun getMultipleEpisode(ids: String, callback: (ApiResult<List<ResultsEpisode>>) -> Unit) {
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

    //    suspend fun getMultipleEpisode(ids: String): ApiResult<List<ResultsEpisode>> {
//        val call = rickAndMortyApi.getMultipleEpisodes(ids)
//        return try {
//            ApiResult.Success(call)
//        } catch (e: Exception) {
//            ApiResult.Error(e.message, e)
//        }
//    }
    fun getMultipleCharacters(ids: String): Call<List<ResultsCharacter>> = rickAndMortyApi.getMultipleCharacters(ids)

    fun getAllEpisodes(page: Int): Call<Episodes> = rickAndMortyApi.getAllEpisodes(page)
//    fun getEpisodeInfo(id: Int): Call<ResultsEpisode> = rickAndMortyApi.getEpisodeInfo(id)

    fun getAllLocations(page: Int): Call<Locations> = rickAndMortyApi.getAllLocations(page)
    fun getLocationInfo(id: Int): Call<ResultsLocation> = rickAndMortyApi.getLocationInfo(id)

}