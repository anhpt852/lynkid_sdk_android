package vn.linkid.sdk.models.auth

data class RefreshToken(
    val accessToken: String?,
    val refreshToken: String?,
    val isSuccess: Boolean?,
    val message: String?
)