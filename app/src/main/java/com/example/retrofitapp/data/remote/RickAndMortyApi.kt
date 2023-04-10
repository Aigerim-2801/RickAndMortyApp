package com.example.retrofitapp.data.remote

import com.example.retrofitapp.domain.model.character.Characters
import com.example.retrofitapp.domain.model.character.ResultsCharacter
import com.example.retrofitapp.domain.model.episode.Episodes
import com.example.retrofitapp.domain.model.episode.ResultsEpisode
import com.example.retrofitapp.domain.model.location.Locations
import com.example.retrofitapp.domain.model.location.ResultsLocation
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickAndMortyApi {
    @GET("character")
    fun getAllCharacters(@Query("page") page: Int): Call<Characters>

    @GET("character")
    fun getFilteredCharacters(
        @Query("name") name: String,
        @Query("status") status: String,
        @Query("gender") gender: String,
        @Query("species") species: String,
        @Query("page") page: Int
    ): Call<Characters>

    @GET("character/{id}")
    fun getCharacterInfo(@Path("id") id: Int): Call<ResultsCharacter>

    @GET("character/{ids}")
    fun getMultipleCharacters(@Path("ids") ids: String): Call<List<ResultsCharacter>>

    @GET("episode/{ids}")
    fun getMultipleEpisodes(@Path("ids") ids: String): Call<List<ResultsEpisode>>

    @GET("episode")
    fun getAllEpisodes(@Query("page") page: Int): Call<Episodes>

    @GET("episode/{id}")
    fun getEpisodeInfo(@Path("id") id: Int): Call<ResultsEpisode>

    @GET("location")
    fun getAllLocations(@Query("page") page: Int): Call<Locations>

    @GET("location/{id}")
    fun getLocationInfo(@Path("id") id: Int): Call<ResultsLocation>
}