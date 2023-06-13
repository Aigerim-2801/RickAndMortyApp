package com.example.retrofitapp.data.utils

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.retrofitapp.data.remote.CharactersDao
import com.example.retrofitapp.domain.model.character.ResultsCharacter

@Database(
    version = 3,
    entities = [
        ResultsCharacter::class
    ]
)
@TypeConverters(OriginTypeConverter::class)
abstract class CharactersDatabase : RoomDatabase() {

    abstract fun getCharactersDao(): CharactersDao

}