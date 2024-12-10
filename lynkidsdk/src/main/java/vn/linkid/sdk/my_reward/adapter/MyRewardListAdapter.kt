package vn.linkid.sdk.my_reward.adapter

import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vn.linkid.sdk.R
import vn.linkid.sdk.databinding.ItemMyRewardBinding
import vn.linkid.sdk.models.my_reward.GiftInfoItem
import vn.linkid.sdk.models.my_reward.RewardUsedStatus
import vn.linkid.sdk.models.my_reward.WhyHaveRewardType
import vn.linkid.sdk.utils.dpToPx
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
            ): Boolean = oldItem.giftTransaction?.code == newItem.giftTransaction?.code

            override fun areContentsTheSame(
                oldItem: GiftInfoItem,
                newItem: GiftInfoItem,
            ): Boolean = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyRewardListViewHolder {
        val binding = ItemMyRewardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
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
            val isEgift = giftInfoItem.eGift != null;
            val isShowReceiveReward =
                isEgift && ((giftInfoItem.giftTransaction?.whyHaveIt ?: "") == "RECEIVED")
            val nameTag: String = giftInfoItem.giftTransaction?.nameTag ?: "Quà tặng"

            val isShowExpiredDate = tab == 0 && isEgift
            val isShowAfterExpiredDate = (tab == 1) && isEgift &&
                    (giftInfoItem.eGift?.usedStatus == RewardUsedStatus.EXPIRED ||
                            giftInfoItem.eGift?.usedStatus == RewardUsedStatus.USED ||
                            giftInfoItem.giftTransaction?.whyHaveIt == WhyHaveRewardType.SENT ||
                            giftInfoItem.giftTransaction?.isTimer == true);


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
                Glide.with(imgBrand).load(giftInfoItem.brandInfo?.brandImage).circleCrop()
                    .placeholder(R.drawable.img_lynkid).into(imgBrand)
                if (tab == 1) {
                    val colorMatrix = ColorMatrix();
                    colorMatrix.setSaturation(0.0f);
                    val filter = ColorMatrixColorFilter(colorMatrix);
                    imgBrand.colorFilter = filter;
                }
                txtGiftName.text = giftInfoItem.giftTransaction?.giftName
                txtBrand.text = if ((giftInfoItem.brandInfo?.brandName
                        ?: "").isEmpty()
                ) "THƯƠNG HIỆU KHÁC" else giftInfoItem.brandInfo?.brandName?.uppercase()
                txtExpireDate.text = ""
                if (tab == 0) {
                    txtAction.visibility = View.VISIBLE
                    if (giftInfoItem.eGift != null) {
                        txtAction.text = "DÙNG NGAY"
                    } else {
                        txtAction.text = "THEO DÕI"
                    }
                } else {
                    txtAction.visibility = View.GONE
                }
                if (isShowExpiredDate) {
                    txtExpireDate.visibility = View.VISIBLE
                    txtExpireDate.text = if (remainingDay in 1..10) {
                        "Hết hạn sau $remainingDay ngày"
                    } else if (remainingHour in 1..23) {
                        "Hết hạn sau $remainingHour giờ"
                    } else {
                        "HSD: $expireDateString"
                    }
                } else if (isShowAfterExpiredDate) {
                    txtExpireDate.visibility = View.VISIBLE
                    txtExpireDate.text = "${
                        when {
                            !giftInfoItem?.giftTransaction?.eGiftUsedAt.isNullOrEmpty() -> "Đã dùng vào"
                            giftInfoItem?.eGift?.usedStatus == RewardUsedStatus.EXPIRED -> "Hết hạn vào"
                            giftInfoItem?.giftTransaction?.whyHaveIt == WhyHaveRewardType.SENT -> "Đã tặng vào"
                            giftInfoItem?.giftTransaction?.isTimer == true -> "Sẽ tặng vào"
                            else -> ""
                        }
                    } ${
                        when {
                            !giftInfoItem?.giftTransaction?.eGiftUsedAt.isNullOrEmpty() ->
                                giftInfoItem?.giftTransaction?.eGiftUsedAt ?: ""

                            giftInfoItem?.eGift?.usedStatus == RewardUsedStatus.EXPIRED ->
                                giftInfoItem?.eGift?.expiredDate ?: ""

                            giftInfoItem?.giftTransaction?.whyHaveIt == WhyHaveRewardType.SENT ||
                                    giftInfoItem?.giftTransaction?.isTimer == true ->
                                giftInfoItem?.giftTransaction?.transferTime ?: ""

                            else -> {
                                txtExpireDate.visibility = View.GONE
                                ""
                            }
                        }
                    }"
                } else {
                    txtExpireDate.visibility = View.GONE
                }



                if (isShowReceiveReward) {
                    viewTag.visibility = View.VISIBLE
                    viewTag.background =
                        ContextCompat.getDrawable(binding.root.context, R.drawable.bg_my_reward_tag)
                    txtTag.text = nameTag

                } else if (!isEgift) {
                    viewTag.visibility = View.VISIBLE
                    Log.i("MyRewardListAdapter", "bind: ${giftInfoItem.giftTransaction?.status}")
                    Log.i(
                        "MyRewardListAdapter", "bind2: ${
                            when (giftInfoItem.giftTransaction?.status) {
                                "Pending", "Waiting" -> "Chờ xác nhận"
                                "Approved", "Confirmed" -> "Đã xác nhận"
                                "Rejected", "Canceled" -> "Đã hủy"
                                "Delivered" -> "Đã giao hàng"
                                "Delivering" -> "Đang giao"
                                "Returned" -> "Đã trả hàng"
                                "Returning" -> "Đang trả hàng"
                                else -> "Quà tặng"
                            }
                        }"
                    )
                    txtTag.text = when (giftInfoItem.giftTransaction?.status) {
                        "Pending", "Waiting" -> "Chờ xác nhận"
                        "Approved", "Confirmed" -> "Đã xác nhận"
                        "Rejected", "Canceled" -> "Đã hủy"
                        "Delivered" -> "Đã giao hàng"
                        "Delivering" -> "Đang giao"
                        "Returned" -> "Đã trả hàng"
                        "Returning" -> "Đang trả hàng"
                        else -> "Quà tặng"
                    }
                    val drawable = GradientDrawable().apply {
                        shape = GradientDrawable.RECTANGLE
                        val color = when (giftInfoItem.giftTransaction?.status) {
                            "Pending", "Waiting" -> Color.parseColor("#FFAD33")
                            "Approved", "Confirmed" -> Color.parseColor("#007AFF")
                            "Rejected", "Canceled" -> Color.parseColor("#F5574E")
                            "Delivered" -> Color.parseColor("#34C759")
                            "Delivering" -> Color.parseColor("#34C759")
                            "Returned" -> Color.parseColor("#34C759")
                            "Returning" -> Color.parseColor("#007AFF")
                            else -> null
                        }
                        if (color != null) {
                            setColor(color)
                        }
                        cornerRadii = floatArrayOf(
                            0f,
                            0f, // top-left
                            0f,
                            0f, // top-right
                            binding.root.context.dpToPx(8).toFloat(),
                            binding.root.context.dpToPx(8).toFloat(), // bottom-right
                            binding.root.context.dpToPx(8).toFloat(),
                            binding.root.context.dpToPx(8).toFloat()  // bottom-left
                        )
                    }
                    viewTag.background = if (txtTag.text == "Quà tặng") ContextCompat.getDrawable(
                        binding.root.context,
                        R.drawable.bg_my_reward_tag
                    ) else drawable
                } else {
                    viewTag.visibility = View.GONE
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