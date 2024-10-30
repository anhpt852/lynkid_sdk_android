package vn.linkid.sdk.imedia.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import vn.linkid.sdk.R
import vn.linkid.sdk.databinding.ItemImediaBrandBinding
import vn.linkid.sdk.databinding.ItemImediaDialogBrandBinding
import vn.linkid.sdk.models.imedia.GetThirdPartyBrandByVendor

class DialogIMediaBrandAdapter(
    private val selectedBrand: GetThirdPartyBrandByVendor,
    private val iMediaBrandList: List<GetThirdPartyBrandByVendor>,
) :
    RecyclerView.Adapter<DialogIMediaBrandAdapter.ItemIMediaBrandViewHolder>() {

    var onItemClick: ((GetThirdPartyBrandByVendor?) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemIMediaBrandViewHolder {
        val binding =
            ItemImediaDialogBrandBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemIMediaBrandViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemIMediaBrandViewHolder, position: Int) {
        val currentPosition = position
        val brand = iMediaBrandList[currentPosition]
        holder.bind(brand, brand.brandMapping?.brandId == selectedBrand.brandMapping?.brandId)
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(brand)
        }
    }

    override fun getItemCount(): Int = iMediaBrandList.size

    inner class ItemIMediaBrandViewHolder(private val binding: ItemImediaDialogBrandBinding) :
        ViewHolder(binding.root) {
        fun bind(iMediaBrand: GetThirdPartyBrandByVendor, isSelected: Boolean) {
            binding.apply {
                root.isSelected = isSelected
                imgCheck.visibility = if (isSelected) View.VISIBLE else View.GONE
                Glide.with(root.context).load(iMediaBrand.brandMapping?.linkLogo)
                    .error(R.drawable.img_lynkid).into(imgBrand)
                imgBrand.clipToOutline = true
                txtBrandName.text = iMediaBrand.brandMapping?.brandName
                txtBrandName.setTextColor(
                    if (isSelected) Color.parseColor("#663692") else Color.parseColor(
                        "#242424"
                    )
                )
                imgBrand.clipToOutline = true
            }
        }
    }

}