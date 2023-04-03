package com.example.retrofitapp.sources.episode

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofitapp.databinding.EpisodeDetailBinding
import com.example.retrofitapp.sources.episode.data.ResultsEpisode

class EpisodeAdapter : RecyclerView.Adapter<EpisodeAdapter.EpisodeViewHolder>() {

    var onEpisodeClick: ((ResultsEpisode) -> Unit)? = null

    private val listOfEpisodes = mutableListOf<ResultsEpisode>()

    fun submit(episodes: List<ResultsEpisode>){
        listOfEpisodes.addAll(episodes)
        notifyDataSetChanged()
    }

    class EpisodeViewHolder(val binding: EpisodeDetailBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
        val binding = EpisodeDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EpisodeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        val item = listOfEpisodes[position]
        with(holder) {
            binding.episode.text = item.episode
            binding.nameEpisode.text = item.name
            binding.airDate.text = item.air_date
        }
        holder.binding.btnEpisode.setOnClickListener {
            onEpisodeClick?.invoke(item)
        }
    }

    override fun getItemCount(): Int {
        return listOfEpisodes.size
    }

}