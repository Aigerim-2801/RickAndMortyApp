package com.example.retrofitapp.adapters

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.retrofitapp.R
import com.example.retrofitapp.databinding.CharacterItemBinding
import com.example.retrofitapp.domain.model.character.ResultsCharacter


class CharacterAdapter : ListAdapter<ResultsCharacter, CharacterAdapter.CharacterViewHolder>(
    CharacterDiffCallback()
) {
    var onCharacterClick: ((ResultsCharacter) -> Unit)? = null

    var onFavoriteClick: ((ResultsCharacter) -> Unit)? = null

    class CharacterViewHolder(val binding: CharacterItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val binding = CharacterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CharacterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val character = getItem(position)
        holder.binding.apply {
            nameTv.text = character.name
            status.text = character.status
            Glide.with(holder.itemView.context).load(character.image)
                .into(imageView)

            when (character.status) {
                "Alive" -> {
                    statusImg.setImageResource(R.drawable.status_alive)
                }
                "Dead" -> {
                    statusImg.setImageResource(R.drawable.status_dead)
                }
                else -> {
                    statusImg.setImageResource(R.drawable.status_unknown)
                }
            }

            if (character.isFavorite) {
                favoriteBtn.imageTintList = ColorStateList.valueOf(Color.RED)
            } else {
                favoriteBtn.imageTintList = ColorStateList.valueOf(Color.BLACK)
            }

            favoriteBtn.setOnClickListener {
                favoriteBtn.imageTintList = ColorStateList.valueOf(Color.RED)
                onFavoriteClick?.invoke(character)
            }
        }

        holder.itemView.setOnClickListener {
            onCharacterClick?.invoke(character)
        }
    }

    class CharacterDiffCallback : DiffUtil.ItemCallback<ResultsCharacter>() {
        override fun areItemsTheSame(
            oldItem: ResultsCharacter,
            newItem: ResultsCharacter
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ResultsCharacter,
            newItem: ResultsCharacter
        ): Boolean {
            return oldItem == newItem
        }
    }
}


