package com.linkid.sdk.models.auth

data class ConnectionInfo(
    val isExisting: Boolean?,
    val connectedToPhone: String?,
    val connectedToMemberCode: String?,
)
