package vn.linkid.sdk.models.member

data class MemberResponseModel(
    val data: Member?,
    val message: String?,
    val isSuccess: Boolean?
)