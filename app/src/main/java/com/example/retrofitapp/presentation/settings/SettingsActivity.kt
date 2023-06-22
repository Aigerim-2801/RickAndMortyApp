package com.example.retrofitapp.presentation.settings

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import com.example.retrofitapp.databinding.ActivitySettingsBinding
import com.example.retrofitapp.presentation.location.LocationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.themeSwitch.isChecked = viewModel.getThemePreference()

        binding.themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.saveThemePreference(isChecked)
            viewModel.applyTheme(isChecked)
        }
    }

    companion object{
        const val THEME_PREFERENCE_KEY = "theme_preference"
    }
}