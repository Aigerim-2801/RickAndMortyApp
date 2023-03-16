package com.example.retrofitapp.sources

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofitapp.databinding.EpisodesDetailBinding
import com.example.retrofitapp.sources.character.data.Results

class EpisodeAdapter(var character: List<Results>) : RecyclerView.Adapter<EpisodeAdapter.ViewHolder>() {

    class ViewHolder(val binding: EpisodesDetailBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = EpisodesDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = character[position]
        with(holder) {
            for(pos in item.episode) {
                binding.episode.text = pos
            }
        }

        holder.itemView.setOnClickListener { view ->
            val intent = Intent(view.context, ItemDetailActivity::class.java)
            view.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return character.size
    }
}