package com.linkid.sdk.models.gift

data class GiftDiscount(
    val flashSaleProgramName: String?,
    val redeemGiftQuantity: Int?,
    val reductionRate: Int?,
    val remainingQuantityFlashSale: Int?,
    val salePrice: Int?,
    val status: String?,
    val usedQuantityFlashSale: Int?,
    val warningOutOfStock: Boolean?
)