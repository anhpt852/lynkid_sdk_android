package vn.linkid.sdk.gift_usage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vn.linkid.sdk.R
import vn.linkid.sdk.all_gift.adapter.GiftGroupAdapter
import vn.linkid.sdk.databinding.ItemCategoryGiftBinding
import vn.linkid.sdk.databinding.ItemGiftUsageBinding
import vn.linkid.sdk.models.category.Gift
import vn.linkid.sdk.models.gift_usage.GiftUsageAddress
import vn.linkid.sdk.utils.formatPrice

class GiftUsageAdapter() :
    PagingDataAdapter<GiftUsageAddress, GiftUsageAdapter.GiftUsageListViewHolder>(DIFF_CALLBACK) {

    var onItemClick: ((GiftUsageAddress) -> Unit)? = null

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<GiftUsageAddress>() {
            override fun areItemsTheSame(
                oldItem: GiftUsageAddress,
                newItem: GiftUsageAddress,
            ): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: GiftUsageAddress,
                newItem: GiftUsageAddress,
            ): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GiftUsageListViewHolder {
        val binding =
            ItemGiftUsageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GiftUsageListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GiftUsageListViewHolder, position: Int) {
        val gift = getItem(position)
        gift?.let {
            holder.bind(it)
        }
    }

    inner class GiftUsageListViewHolder(private val binding: ItemGiftUsageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(giftUsage: GiftUsageAddress) {
            binding.apply {
                txtAddress.text = giftUsage.address
                itemView.setOnClickListener { onItemClick?.invoke(giftUsage) }
            }
        }

    }
}