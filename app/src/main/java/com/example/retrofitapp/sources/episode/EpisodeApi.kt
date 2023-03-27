package com.example.retrofitapp.sources.episode

import com.example.retrofitapp.sources.character.data.ResultsCharacter
import com.example.retrofitapp.sources.episode.data.Episode
import com.example.retrofitapp.sources.episode.data.ResultsEpisode
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface EpisodeApi {
    @GET("episode")
    suspend fun getEpisode(): Episode

    @GET("episode/{id}")
    fun getEpisodeInfo(@Path("id") id: Int): Call<ResultsEpisode>

    @GET
    fun getCharactersUrl(@Url url: String): Call<ResultsCharacter>
}