package com.example.retrofitapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ListAdapter
import com.example.retrofitapp.databinding.LocationItemBinding
import com.example.retrofitapp.domain.model.location.ResultsLocation

class LocationAdapter : ListAdapter<ResultsLocation, LocationAdapter.LocationViewHolder>(
    LocationDiffCallback()
){

    var onLocationClick: ((ResultsLocation) -> Unit)? = null

    class LocationViewHolder(val binding: LocationItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val binding = LocationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LocationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val location = getItem(position)
        with(holder) {
            binding.nameLocation.text = location.name
            binding.typeLocation.text = location.type
        }

        holder.itemView.setOnClickListener {
            onLocationClick?.invoke(location)
        }
    }

    class LocationDiffCallback : DiffUtil.ItemCallback<ResultsLocation>() {
        override fun areItemsTheSame(
            oldItem: ResultsLocation,
            newItem: ResultsLocation
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ResultsLocation,
            newItem: ResultsLocation
        ): Boolean {
            return oldItem == newItem
        }
    }
}
