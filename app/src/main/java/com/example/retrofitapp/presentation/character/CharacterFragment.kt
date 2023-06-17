package com.example.retrofitapp.presentation.character

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.retrofitapp.R
import com.example.retrofitapp.databinding.CharacterFragmentBinding
import com.example.retrofitapp.presentation.FilterBottomSheetFragment
import com.example.retrofitapp.adapters.CharacterAdapter
import com.example.retrofitapp.data.remote.CharactersDao
import com.example.retrofitapp.data.utils.CharactersDatabase
import com.example.retrofitapp.domain.model.character.FilterCharacters
import com.example.retrofitapp.presentation.SettingsActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch


class CharacterFragment : Fragment() {

    private var _binding: CharacterFragmentBinding? = null
    private val binding get() = _binding!!
    private val characterAdapter = CharacterAdapter()
    private val viewModel: CharacterViewModel by viewModels()

    private var loading = false
    private var previousTotalItemCount = 0
    private var visibleThreshold = 80
    private lateinit var charactersDao: CharactersDao

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

        val db: CharactersDatabase = Room.databaseBuilder(requireContext(), CharactersDatabase::class.java, "CharactersFavoriteDatabase").allowMainThreadQueries().build()

        charactersDao = db.getCharactersDao()
        characterAdapter.onFavoriteClick = { viewModel.checkFlag(it.isFavorite, charactersDao, it) }

        characterAdapter.onCharacterClick = { navigateToDetail(it.id) }

        characterAdapter.onCharacterDeleteClick = { characterItem ->
            val position = viewModel.deleteCharacter(characterItem)
            val snackbar = Snackbar.make(requireView(), R.string.deleted, Snackbar.LENGTH_LONG)
            snackbar.setAction(getString(R.string.undo)) {
                viewModel.undo(position, characterItem)
            }
            snackbarType(snackbar)
        }

        binding.filterBtn.setOnClickListener {
            val filterCharacters = viewModel.filterCharacters
            val bundle = Bundle()
            bundle.putSerializable("FilterCharacters", filterCharacters)

            val filterBottomSheetFragment = FilterBottomSheetFragment()
            filterBottomSheetFragment.arguments = bundle
            filterBottomSheetFragment.show(parentFragmentManager, "FilterBottomSheetDialog")
        }

        binding.openSettingsBtn.setOnClickListener {
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setFilter(filter: FilterCharacters) {
        viewModel.setFilter(filter)
        characterAdapter.submitList(null)
    }

    fun cancelFilter(){
        characterAdapter.submitList(null)
        viewModel.cancelFilter()
    }

    private fun navigateToDetail(id: Int) {
        val fragment = CharacterDetailFragment.startCharacterFragment(id)
        parentFragmentManager.beginTransaction()
            .replace(R.id.recycler_view_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun snackbarType(snackbar: Snackbar){
        snackbar.view.elevation = 100f
        snackbar.view.z = 100f
        snackbar.setActionTextColor(Color.BLACK)
        snackbar.setBackgroundTint(Color.GRAY)
        snackbar.show()
    }
}