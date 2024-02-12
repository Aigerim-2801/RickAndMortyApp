package com.example.retrofitapp.data.repository

import com.example.retrofitapp.data.remote.CharactersDao
import com.example.retrofitapp.domain.model.character.ResultsCharacter
class DatabaseRepository (private val charactersDao: CharactersDao){

    fun insert(characters: ResultsCharacter) = charactersDao.insert(characters)

    fun getAll() = charactersDao.getAll()

    fun delete(characters: ResultsCharacter) = charactersDao.delete(characters)
}