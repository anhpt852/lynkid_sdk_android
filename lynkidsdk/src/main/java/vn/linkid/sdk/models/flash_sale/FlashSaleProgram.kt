package vn.linkid.sdk.models.flash_sale

data class FlashSaleProgram(
    val flashSaleProgramId: Int?,
    val flashSaleProgramCode: String?,
    val flashSaleProgramName: String?,
    val startTime: String?,
    val endTime: String?,
    val status: String?,
    val giftInFlashSaleProgramForViews: List<FlashSaleGift>?,
    val quantityThresholdWarning: Int?,
    val percentThresholdWarning: Int?,
    val image: String?
)
