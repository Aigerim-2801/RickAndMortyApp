package com.example.retrofitapp.sources.character

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofitapp.databinding.EpisodeDetailBinding
import com.example.retrofitapp.sources.RetrofitInstance
import com.example.retrofitapp.sources.episode.data.ResultsEpisode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EpisodeAdapter(var episodes: List<String>) : RecyclerView.Adapter<EpisodeAdapter.ViewHolder>() {

    class ViewHolder(val binding: EpisodeDetailBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = EpisodeDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val episodeUrl = episodes[holder.adapterPosition]

        RetrofitInstance.api_character.getEpisodeUrl(episodeUrl).enqueue(object : Callback<ResultsEpisode> {
            override fun onResponse(call: Call<ResultsEpisode>, response: Response<ResultsEpisode>) {
                if (response.isSuccessful) {
                    val episode = response.body()

                    holder.binding.episode.text = episode?.episode
                    holder.binding.nameEpisode.text = episode?.name
                    holder.binding.airDate.text = episode?.air_date
                }
            }

            override fun onFailure(call: Call<ResultsEpisode>, t: Throwable) {
                Log.e("Error", t.message, t)
            }
        })
    }

    override fun getItemCount(): Int {
        return episodes.size
    }

}