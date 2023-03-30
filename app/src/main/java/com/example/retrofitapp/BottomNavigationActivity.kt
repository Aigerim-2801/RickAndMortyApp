package com.example.retrofitapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.retrofitapp.databinding.ActivityBottomNavigationBinding
import com.example.retrofitapp.sources.character.CharacterFragment
import com.example.retrofitapp.sources.episode.EpisodeFragment
import com.example.retrofitapp.sources.location.LocationFragment

class BottomNavigationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBottomNavigationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBottomNavigationBinding.inflate(layoutInflater)
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
                R.id.character->{
                    val fragment = CharacterFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.recycler_view_container, fragment)
                        .commit()
                    it.isChecked = true
                }
                R.id.location-> {
                    val fragment = LocationFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.recycler_view_container, fragment)
                        .commit()
                    it.isChecked = true

                }
                R.id.episode-> {
                    val fragment = EpisodeFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.recycler_view_container, fragment)
                        .commit()
                    it.isChecked = true

                }
            }
            false
        }

    }
}


