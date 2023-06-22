package com.example.retrofitapp.data.module

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
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitInstance {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()

    @Provides
    @Singleton
    fun provideInstance(okHttpClient: OkHttpClient, BASE_URL: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
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
