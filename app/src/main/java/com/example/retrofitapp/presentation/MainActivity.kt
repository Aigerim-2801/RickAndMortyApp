package com.example.retrofitapp.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.retrofitapp.R
import com.example.retrofitapp.databinding.MainActivityBinding
import com.example.retrofitapp.presentation.character.CharacterDetailFragment
import com.example.retrofitapp.presentation.character.CharacterFragment
import com.example.retrofitapp.presentation.episode.EpisodeDetailFragment
import com.example.retrofitapp.presentation.episode.EpisodeFragment
import com.example.retrofitapp.presentation.location.LocationDetailFragment
import com.example.retrofitapp.presentation.location.LocationFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            val fragment = CharacterFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.recycler_view_container, fragment)
                .commit()
            binding.bottomNavigationView.menu.findItem(R.id.character).isChecked = true
        }

        binding.bottomNavigationView.setOnItemSelectedListener  {
            when(it.itemId){
                R.id.character ->{
                    val fragment = CharacterFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.recycler_view_container, fragment)
                        .commit()
                    it.isChecked = true
                }
                R.id.location -> {
                    val fragment = LocationFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.recycler_view_container, fragment)
                        .commit()
                    it.isChecked = true
                }
                R.id.episode -> {
                    val fragment = EpisodeFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.recycler_view_container, fragment)
                        .commit()
                    it.isChecked = true
                }
                R.id.favorite -> {
                    val fragment = FavoriteFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.recycler_view_container, fragment)
                        .commit()
                    it.isChecked = true
                }
            }
            false
        }
    }

    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.recycler_view_container)
        if (currentFragment is LocationFragment || currentFragment is EpisodeFragment|| currentFragment is LocationDetailFragment || currentFragment is EpisodeDetailFragment || currentFragment is CharacterDetailFragment) {
            val fragment = CharacterFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.recycler_view_container, fragment)
                .commit()
            binding.bottomNavigationView.menu.findItem(R.id.character).isChecked = true
        }
    }
}


