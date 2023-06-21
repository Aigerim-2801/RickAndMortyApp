package com.example.retrofitapp.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.retrofitapp.R
import com.example.retrofitapp.adapters.CharacterAdapter
import com.example.retrofitapp.data.utils.Const
import com.example.retrofitapp.databinding.FavoriteFragmentBinding
import com.example.retrofitapp.presentation.character.ViewModelFactory
import kotlinx.coroutines.launch


class FavoriteFragment : Fragment() {

    private var _binding: FavoriteFragmentBinding? = null
    private val binding get() = _binding!!

    private val characterAdapter = CharacterAdapter()
    private lateinit var viewModel: FavoriteViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FavoriteFragmentBinding.inflate(inflater, container, false)

        val viewModelFactory = ViewModelFactory(0, requireContext())
        viewModel = ViewModelProvider(this, viewModelFactory)[FavoriteViewModel::class.java]

        val layoutManager = GridLayoutManager(requireContext(), 2)
        binding.favoriteRv.layoutManager = layoutManager
        binding.favoriteRv.adapter = characterAdapter

        characterAdapter.onCharacterClick = { character ->
            val bundle = Bundle().apply {
                putInt(Const.CHARACTER_ID, character.id)
            }
            val navController = findNavController()
            navController.navigate(R.id.action_favoriteFragment_to_characterDetailFragment, bundle)
        }

        characterAdapter.onFavoriteClick = {
            viewModel.onFavoriteStateChanged(it.isFavorite, it)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.favoriteCharactersStateFlow.collect{
                characterAdapter.submitList(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
