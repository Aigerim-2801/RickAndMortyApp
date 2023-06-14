package com.example.retrofitapp.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.room.Room
import com.example.retrofitapp.R
import com.example.retrofitapp.adapters.CharacterAdapter
import com.example.retrofitapp.data.remote.CharactersDao
import com.example.retrofitapp.data.utils.CharactersDatabase
import com.example.retrofitapp.databinding.FavoriteFragmentBinding
import com.example.retrofitapp.presentation.character.CharacterViewModel


class FavoriteFragment : Fragment() {

    private var _binding: FavoriteFragmentBinding? = null
    private val binding get() = _binding!!

    private val characterAdapter = CharacterAdapter()
    private val viewModel: CharacterViewModel by viewModels()
    private lateinit var charactersDao: CharactersDao

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FavoriteFragmentBinding.inflate(inflater, container, false)

        val db: CharactersDatabase = Room.databaseBuilder(requireContext(), CharactersDatabase::class.java, "CharactersFavoriteDatabase").allowMainThreadQueries().build()

        charactersDao = db.getCharactersDao()

        val layoutManager = GridLayoutManager(requireContext(), 2)
        binding.favoriteRv.layoutManager = layoutManager
        binding.favoriteRv.adapter = characterAdapter

        update()

        characterAdapter.onCharacterClick = { character ->
            val bundle = Bundle().apply {
                putInt("character_id", character.id)
            }
            val navController = findNavController()
            navController.navigate(R.id.action_favoriteFragment_to_characterDetailFragment, bundle)
        }

        characterAdapter.onFavoriteClick = {
            characterAdapter.submitList(null)
            viewModel.checkFlag(it.isFavorite, charactersDao, it)
        }

        viewModel.characterMutableLiveData.observe(viewLifecycleOwner) {
            characterAdapter.submitList(it)
        }

        return binding.root
    }

    private fun update(){
        characterAdapter.submitList(null)
        viewModel.updateFavoriteCharacters(charactersDao)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
