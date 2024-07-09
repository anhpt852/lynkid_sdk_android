package vn.linkid.sdk.models.flash_sale

data class FlashSaleGift(
    val giftId: Int?,
    val giftDiscountId: Int?,
    val giftCode: String?,
    val giftName: String?,
    val salePrice: Int?,
    val originalPrice: Int?,
    val reductionRate: Int?,
    val reductionRateDisplay: Int?,
    val remainingQuantityFlashSale: Int?,
    val imageLink: String?,
    val usedQuantityFlashSale: Int?,
    val amount: Int?,
    val warningOutOfStock: Boolean?
)
