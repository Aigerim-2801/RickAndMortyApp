package com.example.retrofitapp.sources.location

import com.example.retrofitapp.sources.location.data.Location
import retrofit2.http.GET

interface LocationApi {
    @GET("location")
    suspend fun getLocation(): Location
}