package com.example.retrofitapp.sources.episode

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.retrofitapp.R
import com.example.retrofitapp.databinding.EpisodeFragmentBinding
import com.example.retrofitapp.sources.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EpisodeFragment : Fragment(R.layout.episodes_all_details){

    private var _binding: EpisodeFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var episodesAdapter: EpisodesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = EpisodeFragmentBinding.inflate(inflater,container,false);

        val layoutManager = LinearLayoutManager(requireContext())
        binding.episodesRv.layoutManager = layoutManager
        episodesAdapter = EpisodesAdapter(emptyList())
        binding.episodesRv.adapter = episodesAdapter

        episodesAdapter.onEpisodesClick = {

            val intent = Intent(requireContext(), EpisodeCharacterActivity::class.java).apply {
                putExtra("episode_id", it.id)
            }
            startActivity(intent)

        }

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.api_episode.getEpisode()
                withContext(Dispatchers.Main) {
                    episodesAdapter.episodes = response.results

                    val dividerItemDecoration = DividerItemDecoration(binding.episodesRv.context, layoutManager.orientation)
                    dividerItemDecoration.setDrawable(ResourcesCompat.getDrawable(resources, R.drawable.divider_episode_rv, null)!!)
                    binding.episodesRv.addItemDecoration(dividerItemDecoration)


                    episodesAdapter.notifyDataSetChanged()
                    Log.e("RESPONSE", "${response.results}")
                }
            } catch (e: Exception) {
                Log.e("RESPONSE EXCEPTION", "Error getting results of episodes", e)
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}