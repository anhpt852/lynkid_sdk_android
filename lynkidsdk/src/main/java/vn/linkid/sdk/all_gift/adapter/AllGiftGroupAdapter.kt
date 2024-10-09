package vn.linkid.sdk.all_gift.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import vn.linkid.sdk.databinding.BannerInstallAppBigBinding
import vn.linkid.sdk.databinding.ItemAllGiftGroupBinding
import vn.linkid.sdk.home.adapter.HomeGiftAdapter
import vn.linkid.sdk.models.gift.Gift
import vn.linkid.sdk.models.gift.HomeGiftGroup


class AllGiftGroupAdapter(private val giftGroups: List<HomeGiftGroup>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var onItemClick: ((Gift) -> Unit)? = null
    var onViewAllClick: ((HomeGiftGroup) -> Unit)? = null
    var onBannerClick: (() -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            val binding =
                BannerInstallAppBigBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            AllGiftGroupBannerViewHolder(binding)
        } else {
            val binding =
                ItemAllGiftGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            AllGiftGroupViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is AllGiftGroupViewHolder) {
            val truePosition =
                if (giftGroups.isEmpty()) 0 else if (position == 0) 0 else position - 1
            holder.bind(giftGroups[truePosition])
        } else if (holder is AllGiftGroupBannerViewHolder) {
            holder.bind()
        }
    }

    override fun getItemCount(): Int = giftGroups.size + 1

    override fun getItemViewType(position: Int): Int {
        if (giftGroups.isEmpty()) return 0
        else if (position == 1) return 0
        return 1
    }

    inner class AllGiftGroupViewHolder(private val binding: ItemAllGiftGroupBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(giftGroup: HomeGiftGroup) {
            binding.apply {
                txtTitle.text = giftGroup.giftGroup?.name
                listGift.layoutManager =
                    LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
                val adapter = AllGiftListAdapter(giftGroup.gifts ?: listOf())
                adapter.onItemClick = onItemClick
                listGift.adapter = adapter
                btnSeeMore.setOnClickListener {
                    onViewAllClick?.invoke(giftGroup)
                }
            }
        }
    }

    inner class AllGiftGroupBannerViewHolder(private val binding: BannerInstallAppBigBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.apply {
                itemView.setOnClickListener {
                    onBannerClick?.invoke()
                }
            }
        }
    }
}