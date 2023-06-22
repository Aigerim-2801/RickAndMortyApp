package com.example.retrofitapp.presentation.di

import android.app.Application
import android.content.Context
import com.example.retrofitapp.data.remote.RickAndMortyApi
import com.example.retrofitapp.data.repository.RickAndMortyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitInstance {

    private const val BASE_URL = "https://rickandmortyapi.com/api/"

    @Provides
    @Singleton
    @Named(value = "baseUrl")
    fun provideBaseUrl() = BASE_URL

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()

    @Provides
    @Singleton
    fun provideInstance(okHttpClient: OkHttpClient, @Named(value = "baseUrl") baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit : Retrofit): RickAndMortyApi {
        return retrofit.create(RickAndMortyApi::class.java)
    }

    @Provides
    fun provideContext(application: Application): Context {
        return application
    }

    @Provides
    fun provideRepository(api: RickAndMortyApi): RickAndMortyRepository {
        return RickAndMortyRepository(api)
    }

}
