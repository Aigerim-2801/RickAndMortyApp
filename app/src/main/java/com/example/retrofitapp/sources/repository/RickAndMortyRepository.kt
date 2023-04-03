package com.example.retrofitapp.sources.repository

import com.example.retrofitapp.sources.RetrofitInstance
import com.example.retrofitapp.sources.character.data.ResultsCharacter
import com.example.retrofitapp.sources.episode.data.Episode
import com.example.retrofitapp.sources.episode.data.ResultsEpisode
import com.example.retrofitapp.sources.location.data.Location
import com.example.retrofitapp.sources.location.data.ResultsLocation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object RickAndMortyRepository {

    private val rickAndMortyApi = RetrofitInstance.getRickAndMortyApi()

    fun getAllCharacters(page: Int) = rickAndMortyApi.getAllCharacters(page)
    fun getFilteredCharacter(name: String, status: String, gender: String, species: String) = rickAndMortyApi.getFilteredCharacter(name, status, gender, species)
    fun getCharacterInfo(id: Int) = rickAndMortyApi.getCharacterInfo(id)

//    fun getMultipleEpisodes(ids: String): Call<List<ResultsEpisode>> = rickAndMortyApi.getMultipleEpisodes(ids)

    fun getMultipleEpisode(ids: String): ApiResult<List<ResultsEpisode>> {
        val call = rickAndMortyApi.getMultipleEpisodes(ids)
        return try {
            val response = call.execute()
            if (response.isSuccessful) {
                ApiResult.Success(response.body()!!)
            } else {
                ApiResult.Error(response.message(), Throwable())
            }
        } catch (e: Exception) {
            ApiResult.Error(e.message, e)
        }
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

    fun getAllEpisodes(): Call<Episode> = rickAndMortyApi.getAllEpisodes()
    fun getEpisodeInfo(id: Int): Call<ResultsEpisode> = rickAndMortyApi.getEpisodeInfo(id)

    fun getAllLocations(): Call<Location> = rickAndMortyApi.getAllLocations()
    fun getLocationInfo(id: Int): Call<ResultsLocation> = rickAndMortyApi.getLocationInfo(id)

}