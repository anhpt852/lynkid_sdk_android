package vn.linkid.sdk.my_reward.ui

import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vn.linkid.sdk.databinding.ItemMyRewardBinding
import vn.linkid.sdk.models.my_reward.GiftInfoItem
import java.util.Date
import java.util.Locale

class MyRewardListAdapter(
    private var giftList: List<GiftInfoItem> = emptyList(),
    private val tab: Int = 0
) : RecyclerView.Adapter<MyRewardListAdapter.MyRewardListViewHolder>() {

    var onItemClicked: ((GiftInfoItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyRewardListViewHolder {
        val binding = ItemMyRewardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyRewardListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyRewardListViewHolder, position: Int) =
        holder.bind(giftList[position])

    override fun getItemCount(): Int = giftList.size

    fun updateGiftList(newGiftList: List<GiftInfoItem>) {
        val diffCallback = MyRewardListDiffCallback(giftList, newGiftList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        giftList = newGiftList
        diffResult.dispatchUpdatesTo(this)
    }

    inner class MyRewardListViewHolder(private val binding: ItemMyRewardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(giftInfoItem: GiftInfoItem) {
            var expireDateString = ""
            var remainingDay = -1
            var remainingHour = -1
            val expiredString =
                giftInfoItem.eGift?.expiredDate ?: giftInfoItem.giftInfor?.expireDuration ?: ""

            try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                inputFormat.timeZone = TimeZone.getTimeZone("UTC")
                val expireDate = inputFormat.parse(expiredString)?.let {
                    Date(it.time + 7 * 3600 * 1000) // Adjusting for UTC+7
                }

                val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                expireDateString = outputFormat.format(expireDate)

                val currentDate = Date()

                expireDate?.let {
                    val diff = it.time - currentDate.time // Difference in milliseconds
                    remainingDay = (diff / (86400000)).toInt() // 86,400,000 ms per day
                    remainingHour = ((diff * 24) / 86400000).toInt()
                }
            } catch (e: Exception) {
                // Handle the exception or leave as a no-op
            }

            binding.apply {
                Glide.with(imgBrand)
                    .load(giftInfoItem.brandInfo?.brandImage)
                    .circleCrop()
                    .into(imgBrand)
                txtGiftName.text = giftInfoItem.giftInfor?.name
                txtBrand.text = if ((giftInfoItem.brandInfo?.brandName
                        ?: "").isEmpty()
                ) "THƯƠNG HIỆU KHÁC" else giftInfoItem.brandInfo?.brandName?.uppercase()
                if (tab == 0) {
                    if (giftInfoItem.eGift != null) {
                        txtExpireDate.visibility = View.VISIBLE
                        txtExpireDate.text = if (remainingDay in 1..10) {
                            "Hết hạn sau $remainingDay ngày"
                        } else if (remainingHour in 1..23) {
                            "Hết hạn sau $remainingHour giờ"
                        } else {
                            "HSD: $expireDateString"
                        }
                    } else {
                        txtExpireDate.text = ""
                    }
                }
            }
        }
    }

    inner class MyRewardListDiffCallback(
        private val oldList: List<GiftInfoItem>,
        private val newList: List<GiftInfoItem>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].giftTransaction?.code == newList[newItemPosition].giftTransaction?.code
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

}