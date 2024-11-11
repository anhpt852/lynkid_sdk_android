package vn.linkid.sdk.models.gift_usage

import vn.linkid.sdk.models.BaseModel

data class GiftUsageResponseModel(
    val result: GiftUsage?
): BaseModel()


data class GiftUsage(
    val totalCount: Int?,
    val items: List<GiftUsageAddress>?
)
