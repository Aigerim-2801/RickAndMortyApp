package com.example.retrofitapp.presentation.di

import android.content.Context
import android.content.SharedPreferences
import com.example.retrofitapp.presentation.character.CharacterDetailViewModel
import com.example.retrofitapp.presentation.character.CharacterViewModel
import com.example.retrofitapp.presentation.episode.EpisodeDetailViewModel
import com.example.retrofitapp.presentation.episode.EpisodeViewModel
import com.example.retrofitapp.presentation.favorite.FavoriteViewModel
import com.example.retrofitapp.presentation.location.LocationDetailViewModel
import com.example.retrofitapp.presentation.location.LocationViewModel
import com.example.retrofitapp.presentation.settings.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single<SharedPreferences> {
        androidContext().getSharedPreferences("SettingsPrefs", Context.MODE_PRIVATE)
    }

    viewModel{ CharacterViewModel(rickAndMortyRepository = get()) }

    viewModel{ LocationViewModel(rickAndMortyRepository = get()) }

    viewModel{ EpisodeViewModel(rickAndMortyRepository = get()) }

    viewModel{ FavoriteViewModel(databaseRepository = get()) }

    viewModel{ SettingsViewModel(sharedPreferences = get()) }

    viewModel { (characterId: Int) ->
        CharacterDetailViewModel(
            id = characterId,
            rickAndMortyRepository = get()
        )
    }

    viewModel { (locationId: Int) ->
        LocationDetailViewModel(
            id = locationId,
            rickAndMortyRepository = get()
        )
    }

    viewModel { (episodeId: Int) ->
        EpisodeDetailViewModel(
            id = episodeId,
            rickAndMortyRepository = get()
        )
    }
}
