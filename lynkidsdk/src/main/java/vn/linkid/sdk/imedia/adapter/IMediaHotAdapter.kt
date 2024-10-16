package vn.linkid.sdk.imedia.adapter

import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import vn.linkid.sdk.databinding.ItemImediaBinding
import vn.linkid.sdk.databinding.ItemImediaDataBinding
import vn.linkid.sdk.databinding.ItemImediaHotBinding
import vn.linkid.sdk.databinding.ItemImediaHotDataBinding
import vn.linkid.sdk.models.category.Gift
import vn.linkid.sdk.models.gift.GiftDetail
import vn.linkid.sdk.utils.formatPrice

class IMediaHotAdapter(private val iMediaList: List<Gift>, private val type: Int = 0) :
    RecyclerView.Adapter<ViewHolder>() {


    var onItemClick: ((Gift?) -> Unit)? = null
    private var selectedPosition: Int = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        if (type == 0) {
            val binding =
                ItemImediaHotBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ItemIMediaViewHolder(binding)
        }
        val binding =
            ItemImediaHotDataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemIMediaDataViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentPosition = position
        val gift = iMediaList[currentPosition]
        if (type == 0) {
            (holder as ItemIMediaViewHolder).bind(
                iMediaList[currentPosition],
                currentPosition == selectedPosition
            )
        } else {
            (holder as ItemIMediaDataViewHolder).bind(
                iMediaList[currentPosition],
                currentPosition == selectedPosition
            )
        }
        holder.itemView.setOnClickListener {
            val previousSelected = selectedPosition
            if (currentPosition == selectedPosition) {
                // Deselect if clicking on the already selected item
                selectedPosition = RecyclerView.NO_POSITION
                onItemClick?.invoke(null)
            } else {
                selectedPosition = currentPosition
                onItemClick?.invoke(gift)
            }
            notifyItemChanged(previousSelected)
            notifyItemChanged(selectedPosition)
        }
    }

    override fun getItemCount(): Int = iMediaList.size

    inner class ItemIMediaViewHolder(private val binding: ItemImediaHotBinding) :
        ViewHolder(binding.root) {
        fun bind(gift: Gift, isSelected: Boolean) {
            binding.apply {
                root.isSelected = isSelected
                layoutCheck.visibility = if (isSelected) View.VISIBLE else View.GONE
                val fullPrice = gift.giftInfor?.fullPrice ?: 0.0
                val requiredCoin = gift.giftInfor?.requiredCoin ?: 0.0
                val cashBack =
                    ((gift.giftInfor?.commisPercentCategory ?: 0).toDouble()) * fullPrice / 100
                txtPrice.text = fullPrice.formatPrice()
                if (cashBack > 0) {
                    txtCashback.visibility = View.VISIBLE
                    imgCoin.visibility = View.GONE
                    txtRequiredPrice.visibility = View.GONE
                    txtFullPrice.visibility = View.GONE
                    txtCashback.text = "Hoàn ${cashBack.formatPrice()} điểm"
                } else if (requiredCoin < fullPrice) {
                    txtCashback.visibility = View.GONE
                    imgCoin.visibility = View.VISIBLE
                    txtRequiredPrice.visibility = View.VISIBLE
                    txtFullPrice.visibility = View.VISIBLE
                    txtRequiredPrice.text = requiredCoin.formatPrice()
                    txtFullPrice.apply {
                        paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                        text = fullPrice.formatPrice()
                    }
                } else {
                    txtCashback.visibility = View.GONE
                    imgCoin.visibility = View.GONE
                    txtRequiredPrice.visibility = View.GONE
                    txtFullPrice.visibility = View.GONE
                }


            }
        }
    }

    inner class ItemIMediaDataViewHolder(private val binding: ItemImediaHotDataBinding) :
        ViewHolder(binding.root) {
        fun bind(gift: Gift, isSelected: Boolean) {
            binding.apply {
                root.isSelected = isSelected
                layoutCheck.visibility = if (isSelected) View.VISIBLE else View.GONE
                val data: String? = gift.giftInfor?.name?.split("/")?.first()
                val fullPrice = gift.giftInfor?.fullPrice ?: 0.0
                val requiredCoin = gift.giftInfor?.requiredCoin ?: 0.0
                val displayPrice = if (requiredCoin < fullPrice) requiredCoin else fullPrice
                val duration: String? =
                    if (gift.giftInfor?.name?.contains("/") == true) "1 ngày" else gift.giftInfor?.description?.split(
                        ":"
                    )?.lastOrNull()?.trimStart()
                txtBandwidth.text = data
                txtDuration.text = duration
                txtRequiredPrice.text = displayPrice.formatPrice()
                txtFullPrice.apply {
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    text = fullPrice.formatPrice()
                }
            }
        }
    }

    fun clearSelection() {
        val previousSelected = selectedPosition
        selectedPosition = RecyclerView.NO_POSITION
        notifyItemChanged(previousSelected)
    }
}