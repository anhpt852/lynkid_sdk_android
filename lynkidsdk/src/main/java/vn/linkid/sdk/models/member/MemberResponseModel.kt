package vn.linkid.sdk.models.member

import vn.linkid.sdk.BaseModel

data class MemberResponseModel(
    val data: Member?,
    val message: String?,
    val isSuccess: Boolean?
)