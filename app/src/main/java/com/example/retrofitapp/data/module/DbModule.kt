package com.example.retrofitapp.data.module

import android.content.Context
import androidx.room.Room
import com.example.retrofitapp.data.remote.CharactersDao
import com.example.retrofitapp.data.remote.RickAndMortyApi
import com.example.retrofitapp.data.repository.DatabaseRepository
import com.example.retrofitapp.data.repository.RickAndMortyRepository
import com.example.retrofitapp.data.utils.CharactersDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DbModule {

    @Provides
    @Singleton
    fun provide(@ApplicationContext context: Context) = Room.databaseBuilder(
        context, CharactersDatabase::class.java, "CharactersFavoriteDatabase")
        .allowMainThreadQueries()
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideDao(db: CharactersDatabase) = db.getCharactersDao()

    @Provides
    fun provideDBRepository(dao: CharactersDao): DatabaseRepository {
        return DatabaseRepository(dao)
    }
}