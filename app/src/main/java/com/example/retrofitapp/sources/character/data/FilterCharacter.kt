package com.example.retrofitapp.sources.character.data

import java.io.Serializable

data class FilterCharacter(
    var name: String = "",
    var species: String = "",
    var status: Status? = null,
    var gender: Gender? = null
) : Serializable
