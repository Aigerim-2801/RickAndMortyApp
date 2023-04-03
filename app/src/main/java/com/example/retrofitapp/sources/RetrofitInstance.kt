package com.example.retrofitapp.sources

import com.example.retrofitapp.sources.remote.RickAndMortyApi
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

   private fun getInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Const.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getRickAndMortyApi(): RickAndMortyApi {
        return getInstance().create(RickAndMortyApi::class.java)
    }
}
