package com.example.retrofitapp.presentation.settings

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
class SettingsViewModel (
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
//io thread
//interface for sharedp