package com.example.retrofitapp.presentation.di

import android.app.Application
import androidx.room.Room
import com.example.retrofitapp.data.remote.CharactersDao
import com.example.retrofitapp.data.repository.DatabaseRepository
import com.example.retrofitapp.data.utils.CharactersDatabase
import org.koin.dsl.module
fun provideDataBase(application: Application): CharactersDatabase =
    Room.databaseBuilder(
        application,
        CharactersDatabase::class.java,
        "CharactersFavoriteDatabase"
    ).
    allowMainThreadQueries().build()

fun provideDao(db: CharactersDatabase): CharactersDao = db.getCharactersDao()

fun provideDBRepository(dao: CharactersDao): DatabaseRepository {
    return DatabaseRepository(dao)
}

val databaseModule= module {
    single { provideDataBase(get()) }
    single { provideDao(get()) }
    factory { provideDBRepository(get()) }
}