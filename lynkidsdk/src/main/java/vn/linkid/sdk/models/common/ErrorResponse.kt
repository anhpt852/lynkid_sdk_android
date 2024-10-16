package vn.linkid.sdk.models.common

data class ErrorResponse(
    val isSuccess: Boolean?,
    val message: String?,
    val code: String?
)
