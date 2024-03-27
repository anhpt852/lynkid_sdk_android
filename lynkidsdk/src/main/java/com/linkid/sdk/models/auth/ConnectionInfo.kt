package com.linkid.sdk.models.auth

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class ConnectionInfo(
    val isExisting: Boolean?,
    val connectedToPhone: String?,
    val connectedToMemberCode: String?,
) : Parcelable
