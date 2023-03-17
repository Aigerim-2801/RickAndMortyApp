package com.example.retrofitapp.sources.character

import com.example.retrofitapp.sources.character.data.Character
import retrofit2.http.GET
import retrofit2.http.Path

interface CharacterApi {
    @GET("character")
    suspend fun getResults(): Character

//    @GET("character/{id}")
//    suspend fun getCharactersById(@Path("id") id:Int): Character

}