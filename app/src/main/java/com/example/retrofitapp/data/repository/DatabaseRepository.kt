package com.example.retrofitapp.data.repository

import com.example.retrofitapp.data.remote.CharactersDao
import com.example.retrofitapp.domain.model.character.ResultsCharacter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseRepository  @Inject constructor(private val charactersDao: CharactersDao){

    fun insert(characters: ResultsCharacter) = charactersDao.insert(characters)

    fun getAll(): List<ResultsCharacter> = charactersDao.getAll()

    fun delete(characters: ResultsCharacter) = charactersDao.delete(characters)
}