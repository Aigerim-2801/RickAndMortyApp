package com.example.retrofitapp.sources.location.data

import com.example.retrofitapp.sources.character.data.Info

data class Locations(
    val info: Info,
    val results: List<ResultsLocation>
)