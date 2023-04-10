package com.example.retrofitapp.sources.episode.data

import com.example.retrofitapp.sources.character.data.Info

data class Episodes(
    val info: Info,
    val results: List<ResultsEpisode>
)