package com.example.retrofitapp.data.remote

import androidx.room.*
import com.example.retrofitapp.domain.model.character.ResultsCharacter
import kotlinx.coroutines.flow.Flow

@Dao
interface CharactersDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(characters: ResultsCharacter)

    @Query("SELECT * FROM characters WHERE is_favorite = 1")
    fun getAll(): Flow<List<ResultsCharacter>>

    @Delete
    fun delete(characters: ResultsCharacter)
}

