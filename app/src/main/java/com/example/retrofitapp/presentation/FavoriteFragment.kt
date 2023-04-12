package com.example.retrofitapp.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.retrofitapp.adapters.CharacterAdapter
import com.example.retrofitapp.data.utils.DBHelper
import com.example.retrofitapp.databinding.FavoriteFragmentBinding
import com.example.retrofitapp.presentation.character.CharacterViewModel


class FavoriteFragment : Fragment() {

    private var _binding: FavoriteFragmentBinding? = null
    private val binding get() = _binding!!

    private val characterAdapter = CharacterAdapter()
    private val viewModel: CharacterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FavoriteFragmentBinding.inflate(inflater, container, false)

        val layoutManager = GridLayoutManager(requireContext(), 2)
        binding.favoriteRv.layoutManager = layoutManager
        binding.favoriteRv.adapter = characterAdapter

        val dbHelper = DBHelper(requireContext())
        viewModel.updateFavoriteCharacters(dbHelper)

        characterAdapter.onFavoriteClick = { viewModel.checkFlag(it.isFavorite, dbHelper, it) }

        viewModel.characterMutableLiveData.observe(viewLifecycleOwner) {
            characterAdapter.submitList(it)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}