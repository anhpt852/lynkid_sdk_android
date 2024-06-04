package vn.linkid.sdk.my_reward.adapter

import androidx.recyclerview.widget.RecyclerView
import vn.linkid.sdk.databinding.ItemGiftAddressBinding
import vn.linkid.sdk.models.my_reward.GiftAddressItem

class MyRewardDetailAddressAdapter {

    inner class ItemMyRewardDetailAddressViewHolder(private val binding: ItemGiftAddressBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(address: GiftAddressItem) {
            binding.apply {
                txtTitle.text = address.name
                txtAddress.text = address.address
            }
        }
    }

}