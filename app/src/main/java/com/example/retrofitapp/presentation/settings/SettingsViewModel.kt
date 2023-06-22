package com.example.retrofitapp.presentation.settings

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences
): ViewModel() {

    fun saveThemePreference(isDarkMode: Boolean) {
        sharedPreferences.edit().putBoolean(SettingsActivity.THEME_PREFERENCE_KEY, isDarkMode).apply()
    }

    fun getThemePreference() = sharedPreferences.getBoolean(SettingsActivity.THEME_PREFERENCE_KEY, false)

    fun applyTheme(isDarkMode: Boolean) {
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}