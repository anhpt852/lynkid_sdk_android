package com.linkid.sdk.models.gift

import com.linkid.sdk.BaseModel

data class HomeGiftGroupResponseModel(
    val data: HomeGiftGroup?,
    val message: String,
    val isSuccess: Boolean
)