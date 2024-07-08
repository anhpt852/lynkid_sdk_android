package vn.linkid.sdk.models.diamond

import vn.linkid.sdk.models.category.Category
import vn.linkid.sdk.models.gift.GiftInfo

data class DiamondCategory(
    val giftCategory: Category?,
    val giftInfors: List<DiamondGiftInfo>?,
    val totalCountGiftInfors: Int?
)
