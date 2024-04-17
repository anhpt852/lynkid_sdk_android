package vn.linkid.sdk.models.auth

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BasicInfo(
    val name: String?,
    val memberCode: String?,
    val balance: Double?,
    val accessToken: String?,
    val refreshToken: String?
) : Parcelable
