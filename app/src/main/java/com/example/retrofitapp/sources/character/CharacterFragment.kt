package com.example.retrofitapp.sources.character

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofitapp.databinding.ActivityMainBinding
import com.example.retrofitapp.sources.Const
import com.example.retrofitapp.sources.character.data.FilterCharacter
import com.example.retrofitapp.sources.character.viewModel.CharacterViewModel
import kotlinx.coroutines.launch


class CharacterFragment : Fragment() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private val characterAdapter = CharacterAdapter()
    private val viewModel: CharacterViewModel by viewModels()

    private var loading = false
    private var previousTotalItemCount = 0
    private val visibleThreshold = 4

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ActivityMainBinding.inflate(inflater, container, false)

        val layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerview.layoutManager = layoutManager
        binding.recyclerview.adapter = characterAdapter

        viewModel.characterMutableLiveData.observe(viewLifecycleOwner) {
            characterAdapter.submit(it)
        }

        binding.recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                if (loading && totalItemCount > previousTotalItemCount) {
                    loading = false
                    previousTotalItemCount = totalItemCount
                }
                if (!loading && (lastVisibleItemPosition + visibleThreshold) >= totalItemCount) {
                    loadMore()
                    loading = true
                }
            }
        })

        characterAdapter.onCharacterClick = { navigateToDetail(it.id) }

        binding.filter.setOnClickListener {
            val filterBottomSheet = BottomSheetCharacter()
            filterBottomSheet.show(parentFragmentManager, Const.FILTER_BOTTOM_SHEET)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setFilter(filter: FilterCharacter) {
        lifecycleScope.launch {
            try {
                viewModel.getFilteredCharacter(
                    filter.name,
                    filter.status.toString(),
                    filter.gender.toString(),
                    filter.species
                )
                characterAdapter.reset()
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    private fun navigateToDetail(id: Int) {
        val intent = CharacterDetailActivity.startCharacterDetailIntent(requireContext(), id)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun loadMore() {
        viewModel.getAllCharacters()
    }
}