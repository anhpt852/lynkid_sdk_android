package com.linkid.sdk.models.auth

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ConnectedMemberAuthToken(
    val data: ConnectedMember?,
    val message: String,
    val isSuccess: Boolean
) : Parcelable

@Parcelize
data class ConnectedMember(
    val phoneNumber: String?,
    val cif: String?,
    val merchantId: Int?,
    val isExisting: Boolean?,
    val basicInfo: BasicInfo?,
    val connectionInfo: ConnectionInfo?
) : Parcelable