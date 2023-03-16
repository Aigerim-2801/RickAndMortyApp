package com.example.retrofitapp.sources.character

import com.example.retrofitapp.sources.character.data.Character
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CharacterApi {
    @GET("character")
    suspend fun getResults(): Character
}