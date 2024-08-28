package vn.linkid.sdk.models.gift

import vn.linkid.sdk.models.BaseModel
import vn.linkid.sdk.models.category.Gift

data class GiftGroupResponseModel(
    val result: GiftGroup?
) : BaseModel()


data class GiftGroup(
    val items: List<Gift>?,
    val masDiscount: Double?
)
