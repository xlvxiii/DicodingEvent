package com.example.dicodingevent.ui.favorite

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.dicodingevent.R
import com.example.dicodingevent.data.local.entity.EventEntity
import com.example.dicodingevent.databinding.ItemFavoriteEventBinding

class FavoriteEventAdapter(private val onFavoriteClick: (EventEntity) -> Unit) : ListAdapter<EventEntity, FavoriteEventAdapter.MyViewHolder>(
    DIFF_CALLBACK) {
    class MyViewHolder(val binding: ItemFavoriteEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: EventEntity) {
            binding.tvEventName.text = event.name
            binding.tvSummary.text = event.summary
            Glide.with(itemView.context).load(event.mediaCover).apply(RequestOptions.placeholderOf(R.drawable.baseline_refresh_24).error(R.drawable.baseline_broken_image_24)).into(binding.imgEventPhoto)
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MyViewHolder {
        val binding = ItemFavoriteEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)

        val ivFavorite = holder.binding.ivFavorite
        if (event.isFavorite) {
            ivFavorite.setImageDrawable(ContextCompat.getDrawable(ivFavorite.context, R.drawable.baseline_favorite_24))
        } else {
            ivFavorite.setImageDrawable(ContextCompat.getDrawable(ivFavorite.context,R.drawable.baseline_favorite_border_24))
        }

        ivFavorite.setOnClickListener {
            onFavoriteClick(event)
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<EventEntity> =
            object : DiffUtil.ItemCallback<EventEntity>() {
                override fun areItemsTheSame(oldEvent: EventEntity, newEvent: EventEntity): Boolean {
                    return oldEvent.id == newEvent.id
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(
                    oldItem: EventEntity,
                    newItem: EventEntity
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }

}
