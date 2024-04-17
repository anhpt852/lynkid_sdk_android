package vn.linkid.sdk.models.auth

data class MemberAuthToken(
    val data: MemberAuth?,
    val message: String?,
    val isSuccess: Boolean?
)

data class MemberAuth(
    val newAccessToken: MemberToken?,
    val seedTokenReplacement: MemberToken?
)

data class MemberToken(
    val accessToken: String?,
    val refreshToken: String?
)