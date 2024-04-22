package vn.linkid.sdk.models.gift

import vn.linkid.sdk.BaseModel
import vn.linkid.sdk.models.auth.MemberAuth

data class AllGiftGroupResponseModel(
    val data: AllGiftGroup?,
    val message: String?,
    val isSuccess: Boolean?
)