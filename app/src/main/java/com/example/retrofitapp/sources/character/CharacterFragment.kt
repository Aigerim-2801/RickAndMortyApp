package com.example.retrofitapp.sources.character

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofitapp.databinding.ActivityMainBinding
import com.example.retrofitapp.sources.RetrofitInstance
import com.example.retrofitapp.sources.character.data.FilterCharacter
import com.example.retrofitapp.sources.character.data.ResultsCharacter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class CharacterFragment : Fragment() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private  val characterAdapter= CharacterAdapter()

    private var currentPage: Int = 1
    private var previousTotalItemCount = 0
    private var loading = true
    private val visibleThreshold = 4
    private val startingPageIndex = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ActivityMainBinding.inflate(inflater,container,false);

        val layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerview.layoutManager = layoutManager
        binding.recyclerview.adapter = characterAdapter

        binding.recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                if (loading && totalItemCount > previousTotalItemCount) {
                    loading = false
                    previousTotalItemCount = totalItemCount
                }

                if (!loading && (lastVisibleItemPosition + visibleThreshold) >= totalItemCount) {
                    currentPage++
                    lifecycleScope.launch { loadMoreData() }
                    loading = true
                }

            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }
        })


        characterAdapter.onCharacterClick = {
            val intent = Intent(requireContext(), CharacterDetailActivity::class.java).apply {
                putExtra("character_id", it.id)
            }
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }


        binding.filter.setOnClickListener {
            val filterBottomSheet = BottomSheetCharacter()
            filterBottomSheet.show(parentFragmentManager, "FilterBottomSheetDialog")
        }


        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.api_character.getCharacter(currentPage)
                withContext(Dispatchers.Main) {
                    characterAdapter.submit(response.results)
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

    fun setFilter(filter: FilterCharacter) {
        lifecycleScope.launch {
            try {
                val character = RetrofitInstance.api_character.getFilteredCharacter(filter.name, filter.status.toString(), filter.gender.toString(), filter.species)
                characterAdapter.reset()
                characterAdapter.submit(character.results)
            } catch (e: Exception) { }
        }
    }



    private suspend fun loadMoreData() {
        currentPage++
        val newData = loadNextData()
        characterAdapter.submit(newData)
        loading = false
    }

    private suspend fun loadNextData(): List<ResultsCharacter> {
        val response = RetrofitInstance.api_character.getCharacter(currentPage)
        return response.results
    }
}