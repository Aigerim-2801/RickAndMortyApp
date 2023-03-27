package com.example.retrofitapp.sources.character

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.retrofitapp.databinding.ActivityMainBinding
import com.example.retrofitapp.sources.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CharacterFragment : Fragment() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var characterAdapter: CharacterAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ActivityMainBinding.inflate(inflater,container,false);


        val layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerview.layoutManager = layoutManager
        characterAdapter = CharacterAdapter(emptyList())
        binding.recyclerview.adapter = characterAdapter

        characterAdapter.onCharacterClick = {

            val intent = Intent(requireContext(), CharacterDetailActivity::class.java).apply {
                putExtra("character_id", it.id)
            }
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.api_character.getCharacter()
                withContext(Dispatchers.Main) {
                    characterAdapter.character = response.results
                    characterAdapter.notifyDataSetChanged()
                    Log.e("RESPONSE", "${response.results}")
                }
            } catch (e: Exception) {
                Log.e("RESPONSE EXCEPTION", "Error getting results of characters", e)
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}