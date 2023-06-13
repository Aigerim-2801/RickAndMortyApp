package com.example.retrofitapp.data.remote

import androidx.room.*
import com.example.retrofitapp.domain.model.character.ResultsCharacter

@Dao
interface CharactersDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(characters: ResultsCharacter)

    @Query("SELECT * FROM characters WHERE is_favorite = 1")
    fun getAll(): List<ResultsCharacter>

    @Delete
    fun delete(characters: ResultsCharacter)
}

