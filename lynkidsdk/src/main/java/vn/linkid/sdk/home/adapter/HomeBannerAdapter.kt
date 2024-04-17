package vn.linkid.sdk.home.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vn.linkid.sdk.databinding.ItemHomeBannerBinding
import vn.linkid.sdk.models.banner.BannerItemModel

class HomeBannerAdapter(private val banners: List<BannerItemModel>) :
    RecyclerView.Adapter<HomeBannerAdapter.HomeBannerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeBannerViewHolder {
        val binding =
            ItemHomeBannerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeBannerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeBannerViewHolder, position: Int) {
        holder.bind(banners[position])
    }

    override fun getItemCount(): Int = banners.size


    inner class HomeBannerViewHolder(private val binding: ItemHomeBannerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(banner: BannerItemModel) {
            binding.apply {
                Log.d("HomeBannerAdapter", "bind: ${banner.article?.linkAvatar}")
                Glide.with(binding.root).load(banner.article?.linkAvatar ?: "").centerCrop().into(imgBanner)
            }
        }
    }
}