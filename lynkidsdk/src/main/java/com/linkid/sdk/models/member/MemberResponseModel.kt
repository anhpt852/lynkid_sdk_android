package com.linkid.sdk.models.member

import com.linkid.sdk.BaseModel

data class MemberResponseModel(
    val result: Int?,
    val items: Member?
) : BaseModel()