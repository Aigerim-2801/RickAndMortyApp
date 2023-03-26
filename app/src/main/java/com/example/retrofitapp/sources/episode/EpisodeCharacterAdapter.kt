package com.example.retrofitapp.sources.episode

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.retrofitapp.R
import com.example.retrofitapp.databinding.EpisodeCharacterContentBinding
import com.example.retrofitapp.sources.RetrofitInstance
import com.example.retrofitapp.sources.character.data.ResultsCharacter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EpisodeCharacterAdapter(var charactere: List<String>) : RecyclerView.Adapter<EpisodeCharacterAdapter.EpisodeCharacterViewHolder>() {

    class EpisodeCharacterViewHolder(val binding: EpisodeCharacterContentBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeCharacterViewHolder {
        val binding = EpisodeCharacterContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EpisodeCharacterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EpisodeCharacterViewHolder, position: Int) {
        val characterUrl = charactere[holder.adapterPosition]

        RetrofitInstance.api_episode.getCharactersUrl(characterUrl).enqueue(object :
            Callback<ResultsCharacter> {
            override fun onResponse(call: Call<ResultsCharacter>, response: Response<ResultsCharacter>) {
                if (response.isSuccessful) {
                    val resident = response.body()

                    holder.binding.statusEpisodeCharacter.text = resident?.status
                    holder.binding.nameEpisodeCharacter.text = resident?.name
                    Glide.with(holder.itemView.context).load(resident?.image)
                        .into(holder.binding.imageEpisodeCharacter)


                    when (resident?.status) {
                        "Alive" -> {
                            holder.binding.statusImgEpisodeCharacter.setImageResource(R.drawable.status_alive)
                        }
                        "Dead" -> {
                            holder.binding.statusImgEpisodeCharacter.setImageResource(R.drawable.status_dead)
                        }
                        else -> {
                            holder.binding.statusImgEpisodeCharacter.setImageResource(R.drawable.status_unknown)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ResultsCharacter>, t: Throwable) {
                Log.e("Error", t.message, t)
            }
        })
    }

    override fun getItemCount(): Int {
        return charactere.size
    }

}