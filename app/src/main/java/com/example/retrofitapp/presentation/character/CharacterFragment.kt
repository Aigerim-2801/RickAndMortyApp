package com.example.retrofitapp.presentation.character

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofitapp.R
import com.example.retrofitapp.databinding.CharacterFragmentBinding
import com.example.retrofitapp.presentation.filter.FilterBottomSheetFragment
import com.example.retrofitapp.adapters.CharacterAdapter
import com.example.retrofitapp.data.utils.Const
import com.example.retrofitapp.domain.model.character.FilterCharacters
import com.example.retrofitapp.presentation.favorite.FavoriteViewModel
import com.example.retrofitapp.presentation.filter.FilterBottomSheetFragment.Companion.FILTER_KEY
import com.example.retrofitapp.presentation.filter.FilterBottomSheetFragment.Companion.PAIRS_KEY
import com.example.retrofitapp.presentation.filter.FilterBottomSheetFragment.Companion.REQUEST_KEY
import com.example.retrofitapp.presentation.settings.SettingsActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CharacterFragment : Fragment() {

    private var _binding: CharacterFragmentBinding? = null
    private val binding get() = _binding!!
    private val characterAdapter = CharacterAdapter()

    private val viewModel by viewModels<CharacterViewModel>()
    private val favViewModel by viewModels<FavoriteViewModel>()

    private var loading = false
    private var previousTotalItemCount = 0
    private var visibleThreshold = 80

    private var isButtonClickHandled: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CharacterFragmentBinding.inflate(inflater, container, false)

        val layoutManager = GridLayoutManager(requireContext(), 2)
        binding.characterRv.layoutManager = layoutManager
        binding.characterRv.adapter = characterAdapter
        
        binding.characterRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val pastVisibleItems = layoutManager.findLastVisibleItemPosition()
                val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

                if (dy <= 0) return

                if (loading && totalItemCount > previousTotalItemCount){
                    loading = false
                    previousTotalItemCount = totalItemCount
                }

                if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                    viewModel.getCharacters()
                    loading = true
                    previousTotalItemCount = 0
                    previousTotalItemCount = totalItemCount

                }
            }
        })

        characterAdapter.onFavoriteClick = { character->
            favViewModel.onFavoriteStateChanged(character.isFavorite, character)
        }

        characterAdapter.onCharacterClick = { character->
            val bundle = Bundle().apply {
                putInt(Const.CHARACTER_ID, character.id)
            }
            val navController = findNavController()
            navController.navigate(R.id.action_characterFragment_to_characterDetailFragment, bundle)
        }

        binding.filterBtn.setOnClickListener {
            isButtonClickHandled = true

            val filterCharacters = viewModel.filterCharacters
            val bundle = Bundle()
            bundle.putSerializable(FILTER_KEY, filterCharacters)

            val filterBottomSheetFragment = FilterBottomSheetFragment()
            filterBottomSheetFragment.arguments = bundle
            filterBottomSheetFragment.show(parentFragmentManager, "FilterBottomSheetDialog")
        }

        binding.openSettingsBtn.setOnClickListener {
            isButtonClickHandled = true
            val intent = Intent(activity, SettingsActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.charactersStateFlow.collect{
                characterAdapter.submitList(it)
            }
        }
        lifecycleScope.launch {
            favViewModel.favoriteCharactersStateFlow.collect{
                viewModel.onFavoriteStateChanged(it)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(REQUEST_KEY) { _, bundle ->
            val filter = bundle.getSerializable(PAIRS_KEY) as FilterCharacters?
            if(filter != null) {
                setFilter(filter)
            }else{
                cancelFilter()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun isButtonClickHandled(): Boolean {
        return isButtonClickHandled
    }

    private fun setFilter(filter: FilterCharacters) {
        viewModel.setFilter(filter)
        characterAdapter.submitList(null)
    }

    private fun cancelFilter(){
        characterAdapter.submitList(null)
        viewModel.cancelFilter()
    }
}