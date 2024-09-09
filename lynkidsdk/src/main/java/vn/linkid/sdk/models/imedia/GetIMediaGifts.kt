package vn.linkid.sdk.models.imedia

import vn.linkid.sdk.models.gift.GiftDetail

data class GetIMediaGifts(
    val totalCount: Int?,
    val items: List<GiftDetail>?,
    val balanceAbleToCashout: Double?
)
