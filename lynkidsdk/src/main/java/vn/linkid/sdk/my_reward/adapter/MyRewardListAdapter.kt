package vn.linkid.sdk.my_reward.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vn.linkid.sdk.R
import vn.linkid.sdk.databinding.ItemMyRewardBinding
import vn.linkid.sdk.models.my_reward.GiftInfoItem
import vn.linkid.sdk.transaction.adapter.TransactionAdapter
import vn.linkid.sdk.transaction.adapter.TransactionAdapter.Companion
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class MyRewardListAdapter(private val tab: Int = 0) :
    PagingDataAdapter<GiftInfoItem, MyRewardListAdapter.MyRewardListViewHolder>(DIFF_CALLBACK) {

    var onItemClicked: ((GiftInfoItem) -> Unit)? = null


    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<GiftInfoItem>() {
            override fun areItemsTheSame(
                oldItem: GiftInfoItem,
                newItem: GiftInfoItem,
            ): Boolean =
                oldItem.giftTransaction?.code == newItem.giftTransaction?.code

            override fun areContentsTheSame(
                oldItem: GiftInfoItem,
                newItem: GiftInfoItem,
            ): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyRewardListViewHolder {
        val binding = ItemMyRewardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyRewardListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyRewardListViewHolder, position: Int) {
        val giftInfoItem = getItem(position)
        giftInfoItem?.let {
            holder.bind(it)
        }
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
                expireDateString = if (expireDate != null) outputFormat.format(expireDate) else ""

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
                    .placeholder(R.drawable.img_lynkid)
                    .into(imgBrand)
                txtGiftName.text = giftInfoItem.giftTransaction?.giftName
                txtBrand.text = if ((giftInfoItem.brandInfo?.brandName
                        ?: "").isEmpty()
                ) "THƯƠNG HIỆU KHÁC" else giftInfoItem.brandInfo?.brandName?.uppercase()
                txtExpireDate.text = ""
                txtAction.visibility = View.GONE
                if (tab == 0) {
                    txtAction.visibility = View.VISIBLE
                    if (giftInfoItem.eGift != null) {
                        txtExpireDate.visibility = View.VISIBLE
                        txtAction.text = "DÙNG NGAY"
                        txtExpireDate.text = if (remainingDay in 1..10) {
                            "Hết hạn sau $remainingDay ngày"
                        } else if (remainingHour in 1..23) {
                            "Hết hạn sau $remainingHour giờ"
                        } else {
                            "HSD: $expireDateString"
                        }
                    } else {
                        txtAction.text = "THEO DÕI"
                    }
                }
                itemView.setOnClickListener {
                    onItemClicked?.invoke(giftInfoItem)
                }
                txtAction.setOnClickListener {
                    onItemClicked?.invoke(giftInfoItem)
                }
            }
        }
    }

}