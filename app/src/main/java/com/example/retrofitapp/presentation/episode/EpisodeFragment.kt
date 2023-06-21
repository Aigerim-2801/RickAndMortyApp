package com.example.retrofitapp.presentation.episode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofitapp.R
import com.example.retrofitapp.databinding.EpisodeFragmentBinding
import com.example.retrofitapp.adapters.EpisodeAdapter
import com.example.retrofitapp.data.utils.Const
import kotlinx.coroutines.launch

class EpisodeFragment : Fragment(){

    private var _binding: EpisodeFragmentBinding? = null
    private val binding get() = _binding!!

    private val episodeAdapter = EpisodeAdapter()
    private val viewModel: EpisodeViewModel by viewModels()

    private var loading = false
    private var previousTotalItemCount = 0
    private val visibleThreshold = 18

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = EpisodeFragmentBinding.inflate(inflater, container, false)

        val layoutManager = LinearLayoutManager(requireContext())
        binding.episodesRv.layoutManager = layoutManager
        binding.episodesRv.adapter = episodeAdapter

        binding.episodesRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

                if (dy <= 0) return

                if (loading && totalItemCount > previousTotalItemCount){
                    loading = false
                }

                if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                    viewModel.getAllEpisodes()
                    loading = true
                    previousTotalItemCount = totalItemCount

                }
            }
        })

        episodeAdapter.onEpisodeClick = { episode->
            val bundle = Bundle().apply {
                putInt(Const.EPISODE_ID, episode.id)
            }
            val navController = findNavController()
            navController.navigate(R.id.action_episodeFragment_to_episodeDetailFragment, bundle)
        }

        val dividerItemDecoration = DividerItemDecoration(
            binding.episodesRv.context,
            layoutManager.orientation
        )
        dividerItemDecoration.setDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.divider_rv,
                null
            )!!
        )
        binding.episodesRv.addItemDecoration(dividerItemDecoration)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.episodesStateFlow.collect{
                episodeAdapter.submitList(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}