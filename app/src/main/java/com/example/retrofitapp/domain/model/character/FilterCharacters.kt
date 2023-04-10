package com.example.retrofitapp.domain.model.character

import java.io.Serializable

data class FilterCharacters(
    var name: String = "",
    var species: String = "",
    var status: Status? = null,
    var gender: Gender? = null
) : Serializable
