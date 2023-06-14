package com.example.retrofitapp.data.repository

import com.example.retrofitapp.data.utils.RetrofitInstance
import com.example.retrofitapp.domain.model.character.Characters
import com.example.retrofitapp.domain.model.character.ResultsCharacter
import com.example.retrofitapp.domain.model.episode.Episodes
import com.example.retrofitapp.domain.model.episode.ResultsEpisode
import com.example.retrofitapp.domain.model.location.Locations
import com.example.retrofitapp.domain.model.location.ResultsLocation

object RickAndMortyRepository {

    private val rickAndMortyApi = RetrofitInstance.getRickAndMortyApi()

    suspend fun getAllCharacters(page: Int): ApiResult<Characters> {
        return try {
            val response = rickAndMortyApi.getAllCharacters(page)
            ApiResult.Success(response)
        } catch (e: Exception) {
            ApiResult.Error(e.message, e)
        }
    }

     suspend fun getCharacterInfo(id: Int): ApiResult<ResultsCharacter> {
         return try {
             val response = rickAndMortyApi.getCharacterInfo(id)
             ApiResult.Success(response)
         } catch (e: Exception) {
             ApiResult.Error(e.message, e)
         }
    }

     suspend fun getMultipleCharacters(ids: String): ApiResult<List<ResultsCharacter>>{
         return try {
             val response = rickAndMortyApi.getMultipleCharacters(ids)
             ApiResult.Success(response)
         } catch (e: Exception) {
             ApiResult.Error(e.message, e)
         }
    }

     suspend fun getFilteredCharacters(name: String, status: String, gender: String, species: String, page: Int): ApiResult<Characters>{
         return try {
             val response = rickAndMortyApi.getFilteredCharacters(name, status, gender, species, page)
             ApiResult.Success(response)
         } catch (e: Exception) {
             ApiResult.Error(e.message, e)
         }
    }

    suspend fun getAllEpisodes(page: Int): ApiResult<Episodes>{
        return try {
            val response = rickAndMortyApi.getAllEpisodes(page)
            ApiResult.Success(response)
        } catch (e: Exception) {
            ApiResult.Error(e.message, e)
        }
    }

    suspend fun getEpisodeInfo(id: Int): ApiResult<ResultsEpisode>{
        return try {
            val response = rickAndMortyApi.getEpisodeInfo(id)
            ApiResult.Success(response)
        } catch (e: Exception) {
            ApiResult.Error(e.message, e)
        }
    }

    suspend fun getMultipleEpisodes(ids: String): ApiResult<List<ResultsEpisode>>{
        return try {
            val response = rickAndMortyApi.getMultipleEpisodes(ids)
            ApiResult.Success(response)
        } catch (e: Exception) {
            ApiResult.Error(e.message, e)
        }
    }

    suspend fun getAllLocations(page: Int): ApiResult<Locations>{
        return try {
            val response = rickAndMortyApi.getAllLocations(page)
            ApiResult.Success(response)
        } catch (e: Exception) {
            ApiResult.Error(e.message, e)
        }
    }

    suspend fun getLocationInfo(id: Int): ApiResult<ResultsLocation>{
        return try {
            val response = rickAndMortyApi.getLocationInfo(id)
            ApiResult.Success(response)
        } catch (e: Exception) {
            ApiResult.Error(e.message, e)
        }
    }
}