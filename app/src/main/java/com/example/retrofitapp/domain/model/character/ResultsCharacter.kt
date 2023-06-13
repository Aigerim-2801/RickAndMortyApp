package com.example.retrofitapp.domain.model.character

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "characters")
data class ResultsCharacter(
    @PrimaryKey val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    @ColumnInfo(name = "origin_json") val origin: Origin,
    @ColumnInfo(name = "location_json") val location: Location,
    val image: String,
    @ColumnInfo(name = "episode_json") val episode: List<String>,
    val url: String,
    val created: String,
    @ColumnInfo(name = "is_favorite") var isFavorite: Boolean
)
