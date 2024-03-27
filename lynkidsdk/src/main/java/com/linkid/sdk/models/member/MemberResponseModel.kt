package com.linkid.sdk.models.member

import com.linkid.sdk.BaseModel

data class MemberResponseModel(
    val data: Member?,
    val message: String?,
    val isSuccess: Boolean?
)