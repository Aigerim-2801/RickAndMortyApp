package com.example.retrofitapp.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.retrofitapp.R
import com.example.retrofitapp.databinding.MainActivityBinding
import com.example.retrofitapp.presentation.character.CharacterFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.recycler_view_container) as NavHostFragment
        navController = navHostFragment.navController

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setupActionBar(navController)

        binding.bottomNavigationView.setupWithNavController(navController)

        if (savedInstanceState == null) {
            val fragment = CharacterFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.recycler_view_container, fragment)
                .commit()
            binding.bottomNavigationView.menu.findItem(R.id.characterFragment).isChecked = true
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.recycler_view_container)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun setupActionBar(navController: NavController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.characterFragment) {
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
            } else {
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
            }
        }
    }
}

