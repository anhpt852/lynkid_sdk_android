package vn.linkid.sdk.all_gift.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vn.linkid.sdk.R
import vn.linkid.sdk.databinding.ItemAllGiftBinding
import vn.linkid.sdk.databinding.ItemCategoryGiftBinding
import vn.linkid.sdk.models.category.Gift
import vn.linkid.sdk.utils.formatPrice

class GiftGroupAdapter() :
    PagingDataAdapter<Gift, GiftGroupAdapter.GiftGroupListViewHolder>(DIFF_CALLBACK) {

    var onItemClick: ((Gift) -> Unit)? = null

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Gift>() {
            override fun areItemsTheSame(oldItem: Gift, newItem: Gift): Boolean =
                oldItem.giftInfor?.id == newItem.giftInfor?.id

            override fun areContentsTheSame(oldItem: Gift, newItem: Gift): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GiftGroupListViewHolder {
        val binding = ItemCategoryGiftBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GiftGroupListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GiftGroupListViewHolder, position: Int) {
        val gift = getItem(position)
        gift?.let {
            holder.bind(it)
        }
    }

    inner class GiftGroupListViewHolder(private val binding: ItemCategoryGiftBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(gift: Gift) {
            binding.apply {
                Glide.with(root.context)
                    .load(gift.imageLink?.firstOrNull()?.link)
                    .placeholder(R.drawable.img_gift_placeholder)
                    .into(imgGift)
                txtGiftName.text = gift.giftInfor?.name ?: ""
                txtPrice.text = (gift.giftInfor?.requiredCoin ?: 0.0).formatPrice()
                itemView.setOnClickListener { onItemClick?.invoke(gift) }
            }
        }

    }
}