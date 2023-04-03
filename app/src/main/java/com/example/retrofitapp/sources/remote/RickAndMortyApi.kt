package com.example.retrofitapp.sources.remote

import com.example.retrofitapp.sources.character.data.Characters
import com.example.retrofitapp.sources.character.data.ResultsCharacter
import com.example.retrofitapp.sources.episode.data.Episode
import com.example.retrofitapp.sources.episode.data.ResultsEpisode
import com.example.retrofitapp.sources.location.data.Location
import com.example.retrofitapp.sources.location.data.ResultsLocation
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickAndMortyApi {
    @GET("character")
    fun getAllCharacters(@Query("page") page: Int): Call<Characters>

    @GET("character")
    fun getFilteredCharacter(
        @Query("name") name: String,
        @Query("status") status: String,
        @Query("gender") gender: String,
        @Query("species") species: String
    ): Call<Characters>

    @GET("character/{id}")
    fun getCharacterInfo(@Path("id") id: Int): Call<ResultsCharacter>

    @GET("character/{ids}")
    fun getMultipleCharacters(@Path("ids") ids: String): Call<List<ResultsCharacter>>

    @GET("episode/{ids}")
    fun getMultipleEpisodes(@Path("ids") ids: String): Call<List<ResultsEpisode>>

    @GET("episode")
    fun getAllEpisodes(): Call<Episode>

    @GET("episode/{id}")
    fun getEpisodeInfo(@Path("id") id: Int): Call<ResultsEpisode>

    @GET("location")
    fun getAllLocations(): Call<Location>

    @GET("location/{id}")
    fun getLocationInfo(@Path("id") id: Int): Call<ResultsLocation>
}