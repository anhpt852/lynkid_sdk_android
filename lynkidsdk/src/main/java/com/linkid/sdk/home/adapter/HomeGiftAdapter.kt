package com.linkid.sdk.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.linkid.sdk.databinding.ItemHomeGiftBinding
import com.linkid.sdk.formatPrice
import com.linkid.sdk.models.gift.Gift

class HomeGiftAdapter(private val gifts: List<Gift>) :
    RecyclerView.Adapter<HomeGiftAdapter.HomeGiftViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeGiftViewHolder {
        val binding = ItemHomeGiftBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeGiftViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeGiftViewHolder, position: Int) {
        holder.bind(gifts[position])
    }

    override fun getItemCount(): Int = gifts.size
    inner class HomeGiftViewHolder(private val binding: ItemHomeGiftBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(gift: Gift) {
            binding.apply {
                Glide.with(root.context)
                    .load(gift.fullLink)
                    .into(imgGift)
                txtGiftName.text = gift.giftInfo?.name ?: ""
                txtPrice.text = (gift.giftInfo?.requiredCoin ?: 0).formatPrice()
            }
        }

    }
}