package com.linkid.sdk.models.auth

data class ConnectedMemberAuthToken(
    val data: ConnectedMember?,
    val message: String,
    val isSuccess: Boolean
)

data class ConnectedMember(
    val phoneNumber: String?,
    val cif: String?,
    val merchantId: Int?,
    val isExisting: Boolean?,
    val basicInfo: BasicInfo?,
    val connectionInfo: ConnectionInfo?
)