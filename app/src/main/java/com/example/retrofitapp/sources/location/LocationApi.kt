package com.example.retrofitapp.sources.location

import com.example.retrofitapp.sources.character.data.ResultsCharacter
import com.example.retrofitapp.sources.location.data.Location
import com.example.retrofitapp.sources.location.data.ResultsLocation
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface LocationApi {
    @GET("location")
    suspend fun getLocation(): Location

    @GET("location/{id}")
    fun getLocationInfo(@Path("id") id: Int): Call<ResultsLocation>

    @GET
    fun getResidentsUrl(@Url url: String): Call<ResultsCharacter>

}