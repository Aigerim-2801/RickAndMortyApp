package com.example.retrofitapp.presentation.di

import com.example.retrofitapp.data.remote.RickAndMortyApi
import com.example.retrofitapp.data.repository.RickAndMortyRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
fun provideOkHttpClient(): OkHttpClient =
    OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()

fun provideInstance(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl("https://rickandmortyapi.com/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()
}

fun provideApiService(retrofit : Retrofit): RickAndMortyApi {
    return retrofit.create(RickAndMortyApi::class.java)
}

fun provideRepository(api: RickAndMortyApi): RickAndMortyRepository {
    return RickAndMortyRepository(api)
}

val networkModule= module {
    single { provideOkHttpClient() }
    single { provideInstance(get()) }
    single { provideApiService(get()) }
    factory { provideRepository(get()) }
}