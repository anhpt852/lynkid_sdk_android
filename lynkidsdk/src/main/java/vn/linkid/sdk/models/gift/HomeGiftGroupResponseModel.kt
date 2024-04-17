package vn.linkid.sdk.models.gift

import vn.linkid.sdk.BaseModel

data class HomeGiftGroupResponseModel(
    val data: HomeGiftGroup?,
    val message: String,
    val isSuccess: Boolean
)