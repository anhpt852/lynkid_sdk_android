package com.linkid.sdk.models.auth

data class MemberAuthToken(
    val data: MemberAuth?,
    val message: String?,
    val isSuccess: Boolean?
)

data class MemberAuth(
    val newAccessToken: String?,
    val seedTokenReplacement: String?
)