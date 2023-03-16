package com.example.retrofitapp.sources

import com.example.retrofitapp.sources.character.CharacterApi
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Const.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: CharacterApi by lazy {
        retrofit.create(CharacterApi::class.java)
    }

}