package com.example.retrofitapp.sources.episode

import com.example.retrofitapp.sources.episode.data.Episode
import retrofit2.http.GET

interface EpisodeApi {
    @GET("episode")
    suspend fun getEpisode(): Episode
}