package com.example.retrofitapp.data.utils

import androidx.room.TypeConverter
import com.example.retrofitapp.domain.model.character.Location
import com.example.retrofitapp.domain.model.character.Origin
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class OriginTypeConverter {
    private val gson = Gson()

    @TypeConverter
    fun originToJson(origin: Origin): String {
        return gson.toJson(origin)
    }

    @TypeConverter
    fun jsonToOrigin(json: String): Origin {
        return gson.fromJson(json, Origin::class.java)
    }

    @TypeConverter
    fun locationToJson(location: Location): String {
        return gson.toJson(location)
    }

    @TypeConverter
    fun jsonToLocation(json: String): Location {
        return gson.fromJson(json, Location::class.java)
    }

    @TypeConverter
    fun episodeToJson(episode: List<String>): String {
        return gson.toJson(episode)
    }

    @TypeConverter
    fun jsonToEpisode(json: String): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(json, type)
    }
}