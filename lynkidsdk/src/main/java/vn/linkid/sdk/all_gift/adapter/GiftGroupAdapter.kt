package vn.linkid.sdk.all_gift.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vn.linkid.sdk.databinding.ItemAllGiftBinding
import vn.linkid.sdk.utils.formatPrice
import vn.linkid.sdk.models.gift.Gift

class GiftGroupAdapter(private val gifts: List<Gift>) :
    RecyclerView.Adapter<GiftGroupAdapter.AllGiftListViewHolder>() {

        var onItemClick: ((Gift) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllGiftListViewHolder {
        val binding = ItemAllGiftBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AllGiftListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AllGiftListViewHolder, position: Int) {
        holder.bind(gifts[position])
    }

    override fun getItemCount(): Int = gifts.size
    inner class AllGiftListViewHolder(private val binding: ItemAllGiftBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(gift: Gift) {
            binding.apply {
                Glide.with(root.context)
                    .load(gift.fullLink)
                    .into(imgGift)
                txtGiftName.text = gift.giftInfo?.name ?: ""
                txtPrice.text = (gift.giftInfo?.requiredCoin ?: 0.0).formatPrice()
            }
        }

    }
}