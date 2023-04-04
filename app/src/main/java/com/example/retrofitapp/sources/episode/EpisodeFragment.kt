package com.example.retrofitapp.sources.episode

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofitapp.R
import com.example.retrofitapp.databinding.EpisodeFragmentBinding
import com.example.retrofitapp.sources.episode.viewModel.EpisodeViewModel


class EpisodeFragment : Fragment(){

    private var _binding: EpisodeFragmentBinding? = null
    private val binding get() = _binding!!

    private val episodeAdapter = EpisodeAdapter()
    private val viewModel: EpisodeViewModel by viewModels()

    private var loading = false
    private var previousTotalItemCount = 0
    private val visibleThreshold = 4

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

        episodeAdapter.onEpisodeClick = { navigateToDetail(it.id) }

        viewModel.episodes.observe(viewLifecycleOwner) { episodes ->
            episodeAdapter.submit(episodes)
        }

        val dividerItemDecoration = DividerItemDecoration(
            binding.episodesRv.context,
            layoutManager.orientation
        )
        dividerItemDecoration.setDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.divider_episode_rv,
                null
            )!!
        )
        binding.episodesRv.addItemDecoration(dividerItemDecoration)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadMore() {
        viewModel.getAllEpisodes()
    }

    private fun navigateToDetail(id: Int) {
        val intent = EpisodeDetailActivity.startEpisodeIntent(requireContext(), id)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

}