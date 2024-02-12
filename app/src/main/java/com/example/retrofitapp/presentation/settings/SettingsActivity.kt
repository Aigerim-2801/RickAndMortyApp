package com.example.retrofitapp.presentation.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.retrofitapp.databinding.ActivitySettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val viewModel by viewModel<SettingsViewModel>()

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