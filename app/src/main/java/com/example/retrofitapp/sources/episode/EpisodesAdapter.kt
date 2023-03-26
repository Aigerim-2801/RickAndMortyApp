package com.example.retrofitapp.sources.episode

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofitapp.databinding.EpisodesAllDetailsBinding
import com.example.retrofitapp.sources.episode.data.ResultsEpisode

class EpisodesAdapter(var episodes: List<ResultsEpisode>) : RecyclerView.Adapter<EpisodesAdapter.EpisodesViewHolder>() {

    var onEpisodesClick: ((ResultsEpisode) -> Unit)? = null

    class EpisodesViewHolder(val binding: EpisodesAllDetailsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodesViewHolder {
        val binding = EpisodesAllDetailsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EpisodesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EpisodesViewHolder, position: Int) {
        val item = episodes[position]
        with(holder) {
            binding.episodesAll.text = item.episode
            binding.nameEpisodes.text = item.name
            binding.airDateAll.text = item.air_date
        }

        holder.binding.btnEpisode.setOnClickListener {
            onEpisodesClick?.invoke(item)
        }
    }

    override fun getItemCount(): Int {
        return episodes.size
    }
}