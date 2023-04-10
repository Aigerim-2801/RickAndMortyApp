package com.example.retrofitapp.domain.model.episode

import com.example.retrofitapp.domain.model.character.Info

data class Episodes(
    val info: Info,
    val results: List<ResultsEpisode>
)