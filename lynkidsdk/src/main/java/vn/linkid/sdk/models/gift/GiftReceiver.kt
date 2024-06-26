package vn.linkid.sdk.models.gift

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GiftReceiver(
    val name: String?,
    val phone: String?,
    val cityCode: String?,
    val cityName: String?,
    val districtCode: String?,
    val districtName: String?,
    val wardCode: String?,
    val wardName: String?,
    val address: String?,
    val note: String?
): Parcelable
