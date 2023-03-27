package com.example.retrofitapp.sources.character

import com.example.retrofitapp.sources.character.data.Character
import com.example.retrofitapp.sources.character.data.ResultsCharacter
import com.example.retrofitapp.sources.episode.data.ResultsEpisode
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url


interface CharacterApi {
    @GET("character")
    suspend fun getCharacter(): Character

    @GET("character/{id}")
    fun getCharacterInfo(@Path("id") id: Int): Call<ResultsCharacter>

    @GET
    fun getEpisodeUrl(@Url url: String): Call<ResultsEpisode>
}