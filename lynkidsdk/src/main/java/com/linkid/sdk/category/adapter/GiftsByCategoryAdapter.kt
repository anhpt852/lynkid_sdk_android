package com.linkid.sdk.category.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.linkid.sdk.databinding.ItemCategoryGiftBinding
import com.linkid.sdk.formatPrice
import com.linkid.sdk.models.category.Gift

class GiftsByCategoryAdapter(private val gifts: List<Gift>) : RecyclerView.Adapter<GiftsByCategoryAdapter.GiftsByCategoryViewHolder>() {
    var onItemClick: ((Gift) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GiftsByCategoryViewHolder {
        val binding = ItemCategoryGiftBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GiftsByCategoryViewHolder(binding)
    }

    override fun getItemCount(): Int = gifts.size

    override fun onBindViewHolder(holder: GiftsByCategoryViewHolder, position: Int) {
        holder.bind(gifts[position])
    }

    fun updateGifts(newGifts: List<Gift>) {
        val diffCallback = GiftsByCategoryDiffCallback(gifts, newGifts)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class GiftsByCategoryViewHolder(private val binding: ItemCategoryGiftBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(gift: Gift) {
            binding.apply {
                Glide.with(root.context)
                    .load(gift.imageLink?.first() ?: "")
                    .into(imgGift)
                txtGiftName.text = gift.giftInfor?.name ?: ""
                txtPrice.text = (gift.giftInfor?.requiredCoin ?: 0).formatPrice()
                itemView.setOnClickListener {
                    onItemClick?.invoke(gifts[bindingAdapterPosition])
                }
            }
        }

    }


    inner class GiftsByCategoryDiffCallback(
        private val oldList: List<Gift>,
        private val newList: List<Gift>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].giftInfor?.id == newList[newItemPosition].giftInfor?.id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

}