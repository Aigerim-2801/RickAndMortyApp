package com.example.retrofitapp.sources.character.data

data class FilterCharacter(
    val name: String = "",
    val species: String = "",
    val status: Status? = null,
    val gender: Gender? = null
)
