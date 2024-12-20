package vn.linkid.sdk.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vn.linkid.sdk.R
import vn.linkid.sdk.databinding.ItemHomeGiftBinding
import vn.linkid.sdk.utils.formatPrice
import vn.linkid.sdk.models.gift.Gift

class HomeGiftAdapter(private val gifts: List<Gift>) :
    RecyclerView.Adapter<HomeGiftAdapter.HomeGiftViewHolder>() {

    var onItemClickListener: ((Gift) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeGiftViewHolder {
        val binding =
            ItemHomeGiftBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
                    .placeholder(R.drawable.img_gift_placeholder)
                    .into(imgGift)
                txtGiftName.text = gift.giftInfo?.name ?: ""
                txtPrice.text = (gift.giftInfo?.requiredCoin ?: 0.0).formatPrice()
                itemView.setOnClickListener {
                    onItemClickListener?.invoke(gift)
                }
            }
        }

    }
}