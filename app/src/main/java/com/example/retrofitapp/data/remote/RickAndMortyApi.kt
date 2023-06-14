package com.example.retrofitapp.data.remote

import com.example.retrofitapp.domain.model.character.Characters
import com.example.retrofitapp.domain.model.character.ResultsCharacter
import com.example.retrofitapp.domain.model.episode.Episodes
import com.example.retrofitapp.domain.model.episode.ResultsEpisode
import com.example.retrofitapp.domain.model.location.Locations
import com.example.retrofitapp.domain.model.location.ResultsLocation
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickAndMortyApi {
    @GET("character")
    suspend fun getAllCharacters(@Query("page") page: Int): Characters

    @GET("character")
    suspend fun getFilteredCharacters(
        @Query("name") name: String,
        @Query("status") status: String,
        @Query("gender") gender: String,
        @Query("species") species: String,
        @Query("page") page: Int
    ): Characters

    @GET("character/{id}")
    suspend fun getCharacterInfo(@Path("id") id: Int): ResultsCharacter

    @GET("character/{ids}")
    suspend fun getMultipleCharacters(@Path("ids") ids: String): List<ResultsCharacter>

    @GET("episode/{ids}")
    suspend fun getMultipleEpisodes(@Path("ids") ids: String): List<ResultsEpisode>

    @GET("episode")
    suspend fun getAllEpisodes(@Query("page") page: Int): Episodes

    @GET("episode/{id}")
    suspend fun getEpisodeInfo(@Path("id") id: Int): ResultsEpisode

    @GET("location")
    suspend fun getAllLocations(@Query("page") page: Int): Locations

    @GET("location/{id}")
    suspend fun getLocationInfo(@Path("id") id: Int): ResultsLocation
}