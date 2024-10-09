package vn.linkid.sdk.models.imedia

import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.parcelize.Parcelize

@Parcelize
data class TopupRedeemInfo(
    val operation: Int,
    val ownerPhone: String?,
    val accountType: Int?,
    val type: Int?,
    val brand: String?,
    val brandImage: String?,
    val price: Double?,
    val bandwidth: String?,
    val time: String?
) : Parcelable

fun TopupRedeemInfo.toJsonStringForExchange(): String {
    val jsonObject = JsonObject().apply {
        addProperty("operation", operation)
        addProperty("ownerPhone", formatPhoneNumber(ownerPhone))
        addProperty("accountType", accountType)
    }
    return Gson().toJson(jsonObject)
}


private fun formatPhoneNumber(phoneNumber: String?): String =
    if (phoneNumber.isNullOrEmpty()) "" else {
        if (phoneNumber.startsWith("+84")) {
            "0${phoneNumber.substring(3)}"
        } else
            phoneNumber
    }
