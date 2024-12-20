package vn.linkid.sdk.models.gift

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import vn.linkid.sdk.models.my_reward.EGift

@Parcelize
data class GiftExchange(
    val sessionId: String,
    val giftCode: String,
    val totalAmount: Double,
    val description: String,
    val amount: Int?,
    val brandImage: String?,
    val brandName: String?,
    val giftName: String?,
    val expiredString: String?,
    val transactionCode: String?,
    val isEGift: Boolean?
) : Parcelable
