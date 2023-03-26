package com.example.retrofitapp.sources.location

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.retrofitapp.R
import com.example.retrofitapp.databinding.LocationCharacterContentBinding
import com.example.retrofitapp.sources.RetrofitInstance
import com.example.retrofitapp.sources.character.data.ResultsCharacter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LocationCharacterAdapter(var residents: List<String>) : RecyclerView.Adapter<LocationCharacterAdapter.LocationCharacterViewHolder>() {

    class LocationCharacterViewHolder(val binding: LocationCharacterContentBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationCharacterViewHolder {
        val binding = LocationCharacterContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LocationCharacterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LocationCharacterViewHolder, position: Int) {
        val residentsUrl = residents[holder.adapterPosition]

        RetrofitInstance.api_location.getResidentsUrl(residentsUrl).enqueue(object :
            Callback<ResultsCharacter> {
            override fun onResponse(call: Call<ResultsCharacter>, response: Response<ResultsCharacter>) {
                if (response.isSuccessful) {
                    val resident = response.body()

                    holder.binding.statusLocationCharacter.text = resident?.status
                    holder.binding.nameLocationCharacter.text = resident?.name
                    Glide.with(holder.itemView.context).load(resident?.image)
                        .into(holder.binding.imageLocationCharacter)


                    when (resident?.status) {
                        "Alive" -> {
                            holder.binding.statusImgLocationCharacter.setImageResource(R.drawable.status_alive)
                        }
                        "Dead" -> {
                            holder.binding.statusImgLocationCharacter.setImageResource(R.drawable.status_dead)
                        }
                        else -> {
                            holder.binding.statusImgLocationCharacter.setImageResource(R.drawable.status_unknown)
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
        return residents.size
    }

}