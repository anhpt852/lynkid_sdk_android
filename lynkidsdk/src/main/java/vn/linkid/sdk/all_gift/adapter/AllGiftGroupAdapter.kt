package vn.linkid.sdk.all_gift.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import vn.linkid.sdk.all_gift.adapter.AllGiftGroupAdapter.AllGiftGroupViewHolder
import vn.linkid.sdk.databinding.BannerInstallAppBigBinding
import vn.linkid.sdk.databinding.ItemAllGiftGroupBinding
import vn.linkid.sdk.home.adapter.HomeGiftAdapter
import vn.linkid.sdk.models.gift.Gift
import vn.linkid.sdk.models.gift.HomeGiftGroup


class AllGiftGroupAdapter(private val giftGroups: List<HomeGiftGroup>) :
    RecyclerView.Adapter<AllGiftGroupViewHolder>() {
    var onItemClick: ((Gift) -> Unit)? = null
    var onViewAllClick: ((HomeGiftGroup) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllGiftGroupViewHolder {
        val binding =
            ItemAllGiftGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AllGiftGroupViewHolder(binding)

    }

    override fun onBindViewHolder(holder: AllGiftGroupViewHolder, position: Int) {
        val truePosition =
            if (giftGroups.isEmpty()) 0 else if (position == 0) 0 else position - 1
        holder.bind(giftGroups[truePosition])
    }

    override fun getItemCount(): Int = giftGroups.size

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
}