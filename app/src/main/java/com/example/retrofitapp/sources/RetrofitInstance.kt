package com.example.retrofitapp.sources

import com.example.retrofitapp.sources.character.CharacterApi
import com.example.retrofitapp.sources.episode.EpisodeApi
import com.example.retrofitapp.sources.location.LocationApi
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Const.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api_character: CharacterApi by lazy {
        retrofit.create(CharacterApi::class.java)
    }

    val api_location: LocationApi by lazy {
        retrofit.create(LocationApi::class.java)
    }

    val api_episode: EpisodeApi by lazy {
        retrofit.create(EpisodeApi::class.java)
    }
}
