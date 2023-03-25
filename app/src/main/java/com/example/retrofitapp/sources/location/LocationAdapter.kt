package com.example.retrofitapp.sources.location

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofitapp.databinding.LocationDetailBinding
import com.example.retrofitapp.sources.location.data.ResultsLocation

class LocationAdapter(var location: List<ResultsLocation>) : RecyclerView.Adapter<LocationAdapter.LocationViewHolder>() {

    var onLocationClick: ((ResultsLocation) -> Unit)? = null

    class LocationViewHolder(val binding: LocationDetailBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val binding = LocationDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LocationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val item = location[position]
        with(holder) {
            binding.nameLocation.text = item.name
            binding.typeLocation.text = item.type
        }

        holder.itemView.setOnClickListener {
            onLocationClick?.invoke(item)
        }
    }

    override fun getItemCount(): Int {
        return location.size
    }
}
