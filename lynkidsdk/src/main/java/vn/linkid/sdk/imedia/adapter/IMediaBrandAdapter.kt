package vn.linkid.sdk.imedia.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import vn.linkid.sdk.databinding.ItemImediaBrandBinding
import vn.linkid.sdk.models.imedia.GetThirdPartyBrandByVendor

class IMediaBrandAdapter(private val iMediaBrandList: List<GetThirdPartyBrandByVendor>) :
    RecyclerView.Adapter<IMediaBrandAdapter.ItemIMediaBrandViewHolder>() {

    var onItemClick: ((GetThirdPartyBrandByVendor?) -> Unit)? = null
    private var selectedPosition: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemIMediaBrandViewHolder {
        val binding =
            ItemImediaBrandBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemIMediaBrandViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemIMediaBrandViewHolder, position: Int) {
        val currentPosition = position
        val brand = iMediaBrandList[currentPosition]
        holder.bind(brand, currentPosition == selectedPosition)
        holder.itemView.setOnClickListener {
            val previousSelected = selectedPosition
            if (currentPosition != selectedPosition) {
                selectedPosition = currentPosition
                onItemClick?.invoke(brand)
            }
            notifyItemChanged(previousSelected)
            notifyItemChanged(selectedPosition)
        }
    }

    override fun getItemCount(): Int = iMediaBrandList.size

    inner class ItemIMediaBrandViewHolder(private val binding: ItemImediaBrandBinding) :
        ViewHolder(binding.root) {
        fun bind(iMediaBrand: GetThirdPartyBrandByVendor, isSelected: Boolean) {
            binding.apply {
                root.isSelected = isSelected
                layoutCheck.visibility = if (isSelected) View.VISIBLE else View.GONE
                Glide.with(root.context).load(iMediaBrand.brandMapping?.linkLogo).into(imgBrand)
            }
        }
    }

}