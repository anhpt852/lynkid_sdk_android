package vn.linkid.sdk.models.category

import vn.linkid.sdk.models.gift.GiftInfo

data class GiftsByCategoryResponseModel(
    val data: Data?,
    val message: String?,
    val isSuccess: Boolean?
)

data class Data(
    val totalCount: Int?,
    val items: List<Gift>?
)

data class Gift(
    val giftInfor: GiftInfo?,
    val imageLink: List<ImageLink>?,
    val flashSaleProgramInfor: FlashSaleProgramInfo?,
    val giftDiscountInfor: GiftDiscountInfo?
)

data class TargetAudience(
    val code: String?,
    val name: String?,
    val isActive: Boolean?,
    val type: Int?,
    val count: Int?,
    val isFilterProfile: Boolean?,
    val targetAudienceDetail: List<TargetAudienceDetail>?
)

data class TargetAudienceDetail(
    val code: String?,
    val targetAudienceId: Int?,
    val type: String?,
    val fullValue: String?,
    val value: String?,
    val fullValueName: String?,
    val level: String?,
    val segmentId: Int?,
    val id: Int?
)

data class Office(
    val titleCity: String?,
    val address: String?
)

data class GiftGroup(
    val code: String?,
    val name: String?,
    val type: String?,
    val id: Int?
)

data class ImageLink(
    val code: String?,
    val type: String?,
    val link: String?,
    val isActive: Boolean?,
    val ordinal: Int?,
    val fullLink: String?,
    val id: Int?
)

data class FlashSaleProgramInfo(
    val id: Int?,
    val code: String?,
    val name: String?,
    val startTime: String?,
    val endTime: String?,
    val creationTime: String?,
    val creatorUserId: Int?,
    val creatorUserName: String?,
    val status: String?,
    val maxGiftPerCustomer: Int?,
    val displayTime: String?,
    val url: String?,
    val image: String?
)

data class GiftDiscountInfo(
    val salePrice: Double?,
    val remainingQuantityFlashSale: Int?,
    val reductionRate: Int?,
    val warningOutOfStock: Boolean?,
    val usedQuantityFlashSale: Int?,
    val status: String?,
    val reductionRateDisplay: Int?,
    val redeemGiftQuantity: Int?,
    val redeemFlashSaleQuantity: Int?,
    val maxAmountRedeem: Int?
)
