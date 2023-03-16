package com.example.retrofitapp.sources.episode.data

import com.example.retrofitapp.sources.character.data.Info
import com.example.retrofitapp.sources.location.data.ResultsLocation

data class Episode(
    val info: Info,
    val results: List<ResultsEpisode>
)