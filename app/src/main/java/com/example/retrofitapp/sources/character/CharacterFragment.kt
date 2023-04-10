package com.example.retrofitapp.sources.character

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofitapp.R
import com.example.retrofitapp.databinding.CharacterFragmentBinding
import com.example.retrofitapp.sources.character.data.FilterCharacter
import com.example.retrofitapp.sources.character.viewModel.CharacterViewModel
import com.google.android.material.snackbar.Snackbar


class CharacterFragment : Fragment() {

    private var _binding: CharacterFragmentBinding? = null
    private val binding get() = _binding!!
    private val characterAdapter = CharacterAdapter()
    private val viewModel: CharacterViewModel by viewModels()

    private var loading = false
    private var previousTotalItemCount = 0
    private var visibleThreshold = 80

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CharacterFragmentBinding.inflate(inflater, container, false)

        val layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerview.layoutManager = layoutManager
        binding.recyclerview.adapter = characterAdapter

        viewModel.characterMutableLiveData.observe(viewLifecycleOwner) {
            characterAdapter.submitList(it)
        }


        binding.recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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

        characterAdapter.onCharacterClick = { navigateToDetail(it.id) }

        characterAdapter.onCharacterDelete = { characterItem ->
            val position = viewModel.deleteCharacter(characterItem)
            val snackbar = Snackbar.make(requireView(), R.string.deleted, Snackbar.LENGTH_LONG)
            snackbar.setAction("UNDO") {
                viewModel.undo(position, characterItem)
            }
            snackbarType(snackbar)
        }

        binding.filter.setOnClickListener {
            val FILTER_BOTTOM_SHEET = "FilterBottomSheetDialog"

            val filterCharacter = viewModel.filterCharacter
            val bundle = Bundle()
            bundle.putSerializable("FilterCharacter", filterCharacter)
            val filterBottomSheet = BottomSheetCharacter()

            filterBottomSheet.arguments = bundle
            filterBottomSheet.show(parentFragmentManager, FILTER_BOTTOM_SHEET)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setFilter(filter: FilterCharacter) {
        viewModel.setFilter(filter)
        characterAdapter.submitList(null)
    }

    fun cancelFilter(){
        characterAdapter.submitList(null)
        viewModel.cancelFilter()
    }

    private fun navigateToDetail(id: Int) {
        val intent = CharacterDetailActivity.startCharacterDetailIntent(requireContext(), id)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun snackbarType(snackbar: Snackbar){
        snackbar.view.elevation = 100f
        snackbar.view.z = 100f
        snackbar.setActionTextColor(Color.BLACK)
        snackbar.setBackgroundTint(Color.GRAY)
        snackbar.show()
    }
}