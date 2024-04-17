package vn.linkid.sdk.models.gift

data class GiftInfo(
    val code: String?,
    val description: String?,
    val discountPrice: Int?,
    val fullPrice: Int?,
    val id: Int?,
    val name: String?,
    val requiredCoin: Int?,
    val totalWish: Int?
)