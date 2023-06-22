package com.example.retrofitapp.presentation.di

import android.content.Context
import androidx.room.Room
import com.example.retrofitapp.data.remote.CharactersDao
import com.example.retrofitapp.data.repository.DatabaseRepository
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

    private const val dbName = "CharactersFavoriteDatabase"

    @Provides
    @Singleton
    fun provide(@ApplicationContext context: Context) = Room.databaseBuilder(
        context, CharactersDatabase::class.java, dbName)
        .allowMainThreadQueries()
        .build()

    @Provides
    @Singleton
    fun provideDao(db: CharactersDatabase) = db.getCharactersDao()

    @Provides
    fun provideDBRepository(dao: CharactersDao): DatabaseRepository {
        return DatabaseRepository(dao)
    }
}