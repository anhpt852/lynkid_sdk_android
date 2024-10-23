package vn.linkid.sdk.my_reward.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.linkid.sdk.databinding.ItemGiftAddressBinding
import vn.linkid.sdk.models.my_reward.GiftAddressItem
import vn.linkid.sdk.utils.copyToClipboard
import kotlin.math.min

class MyRewardDetailAddressAdapter(private val listAddress: List<GiftAddressItem>) :
    RecyclerView.Adapter<MyRewardDetailAddressAdapter.ItemMyRewardDetailAddressViewHolder>() {

    var onItemClick: ((GiftAddressItem) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemMyRewardDetailAddressViewHolder {
        val binding =
            ItemGiftAddressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemMyRewardDetailAddressViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemMyRewardDetailAddressViewHolder, position: Int) =
        holder.bind(listAddress[position])

    override fun getItemCount(): Int = min(listAddress.size, 5)

    inner class ItemMyRewardDetailAddressViewHolder(private val binding: ItemGiftAddressBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(address: GiftAddressItem) {
            binding.apply {
                txtTitle.text = address.name
                txtAddress.text = address.address
                itemView.setOnClickListener { onItemClick?.invoke(address) }
                btnCopy.setOnClickListener {
                    copyToClipboard(
                        binding.root.context,
                        address.address,
                        "gift_address",
                        "Đã sao chép địa chỉ"
                    )
                }
            }
        }
    }

}