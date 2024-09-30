package vn.linkid.sdk.imedia.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vn.linkid.sdk.R
import vn.linkid.sdk.databinding.ItemMyRewardBinding
import vn.linkid.sdk.diamond.adapter.DiamondGiftsAdapter
import vn.linkid.sdk.diamond.adapter.DiamondGiftsAdapter.Companion
import vn.linkid.sdk.models.category.Gift
import vn.linkid.sdk.models.imedia.GetIMediaHistory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class IMediaHistoryTabAdapter(private val tab: Int) :
    PagingDataAdapter<GetIMediaHistory, IMediaHistoryTabAdapter.IMediaHistoryViewHolder>(
        DIFF_CALLBACK
    ) {
    var onItemClick: ((GetIMediaHistory) -> Unit)? = null

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<GetIMediaHistory>() {
            override fun areItemsTheSame(
                oldItem: GetIMediaHistory,
                newItem: GetIMediaHistory
            ): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: GetIMediaHistory,
                newItem: GetIMediaHistory
            ): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IMediaHistoryViewHolder {
        val binding = ItemMyRewardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return IMediaHistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IMediaHistoryViewHolder, position: Int) {
        val iMediaHistory = getItem(position)
        iMediaHistory?.let {
            holder.bind(it)
        }
    }

    inner class IMediaHistoryViewHolder(private val binding: ItemMyRewardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(iMediaHistory: GetIMediaHistory) {
            var expireDateString = ""
            var remainingDay = -1
            var remainingHour = -1
            val expiredString =
                iMediaHistory.eGiftExpiredDate ?: ""

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
                    .load(iMediaHistory.brandInfo?.brandImage)
                    .circleCrop()
                    .placeholder(R.drawable.img_lynkid)
                    .into(imgBrand)
                txtGiftName.text = iMediaHistory.giftName
                txtBrand.text = if ((iMediaHistory.brandInfo?.brandName
                        ?: "").isEmpty()
                ) "THƯƠNG HIỆU KHÁC" else iMediaHistory.brandInfo?.brandName?.uppercase()
                txtExpireDate.text = ""
                txtAction.visibility = View.GONE
                if (tab == 0) {
                    txtAction.visibility = View.VISIBLE
                    txtExpireDate.visibility = View.VISIBLE
                    txtAction.text = "DÙNG NGAY"
                    txtExpireDate.text = if (remainingDay in 1..10) {
                        "Hết hạn sau $remainingDay ngày"
                    } else if (remainingHour in 1..23) {
                        "Hết hạn sau $remainingHour giờ"
                    } else {
                        "HSD: $expireDateString"
                    }
                }
                itemView.setOnClickListener {
                    onItemClick?.invoke(iMediaHistory)
                }
                txtAction.setOnClickListener {
                    onItemClick?.invoke(iMediaHistory)
                }
            }
        }
    }

}