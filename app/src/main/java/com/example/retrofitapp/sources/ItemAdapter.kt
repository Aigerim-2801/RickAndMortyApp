package com.example.retrofitapp.sources

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.retrofitapp.databinding.ItemsLayoutBinding
import com.example.retrofitapp.sources.character.data.Results


class ItemAdapter(var character: List<Results>) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    var onCharacterClick: ((Results) -> Unit)? = null

    class ViewHolder(val binding: ItemsLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemsLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = character[position]
        with(holder) {
                binding.name.text = item.name
                binding.status.text = item.status
                Glide.with(holder.itemView.context).load(item.image)
                    .into(binding.imageView)
        }

        holder.itemView.setOnClickListener {
            onCharacterClick?.invoke(item)


        }
    }

    override fun getItemCount(): Int {
        return character.size
    }
}
