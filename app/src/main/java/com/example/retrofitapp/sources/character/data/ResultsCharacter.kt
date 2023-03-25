package com.example.retrofitapp.sources.character.data

data class ResultsCharacter(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val origin: Origins,
    val location: Locations,
    val image: String,
    val episode: List<String>,
    val url: String,
    val created: String
)
