package vn.linkid.sdk.models.imedia

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TopupRedeemInfo(
    val operation: Int,
    val ownerPhone: String?,
    val accountType: Int?
): Parcelable
