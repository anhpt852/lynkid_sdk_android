package vn.linkid.sdk.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vn.linkid.sdk.R
import vn.linkid.sdk.databinding.ItemCategoryGiftBinding
import vn.linkid.sdk.models.gift.Gift
import vn.linkid.sdk.utils.formatPrice

class SearchAdapter : PagingDataAdapter<Gift, SearchAdapter.SearchViewHolder>(
    DIFF_CALLBACK
) {
    var onItemClick: ((Gift) -> Unit)? = null


    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Gift>() {
            override fun areItemsTheSame(oldItem: Gift, newItem: Gift): Boolean =
                oldItem.giftInfo?.id == newItem.giftInfo?.id

            override fun areContentsTheSame(oldItem: Gift, newItem: Gift): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchAdapter.SearchViewHolder {
        val binding =
            ItemCategoryGiftBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchAdapter.SearchViewHolder, position: Int) {
        val gift = getItem(position)
        gift?.let {
            holder.bind(it)
        }
    }

    inner class SearchViewHolder(private val binding: ItemCategoryGiftBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(gift: Gift) {
            binding.apply {
                Glide.with(root.context)
                    .load(gift.fullLink ?: "")
                    .placeholder(R.drawable.img_gift_placeholder)
                    .into(imgGift)
                txtGiftName.text = gift.giftInfo?.name ?: ""
                txtPrice.text = (gift.giftInfo?.requiredCoin ?: 0.0).formatPrice()
                itemView.setOnClickListener {
                    onItemClick?.invoke(gift)
                }
            }
        }
    }
}