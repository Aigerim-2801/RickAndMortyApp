package com.example.retrofitapp.domain.model.location

import com.example.retrofitapp.domain.model.character.Info

data class Locations(
    val info: Info,
    val results: List<ResultsLocation>
)