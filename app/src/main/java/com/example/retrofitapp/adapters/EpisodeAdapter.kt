package com.example.retrofitapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofitapp.databinding.EpisodeItemBinding
import com.example.retrofitapp.domain.model.episode.ResultsEpisode

class EpisodeAdapter : ListAdapter<ResultsEpisode, EpisodeAdapter.EpisodeViewHolder>(
    EpisodeDiffCallback()
){

    var onEpisodeClick: ((ResultsEpisode) -> Unit)? = null

    class EpisodeViewHolder(val binding: EpisodeItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
        val binding = EpisodeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EpisodeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        val episode = getItem(position)
        with(holder) {
            binding.episode.text = episode.episode
            binding.nameEpisode.text = episode.name
            binding.airDateEpisode.text = episode.air_date
        }
        holder.binding.btnEpisode.setOnClickListener {
            onEpisodeClick?.invoke(episode)
        }
    }

    class EpisodeDiffCallback : DiffUtil.ItemCallback<ResultsEpisode>() {
        override fun areItemsTheSame(
            oldItem: ResultsEpisode,
            newItem: ResultsEpisode
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ResultsEpisode,
            newItem: ResultsEpisode
        ): Boolean {
            return oldItem == newItem
        }
    }
}