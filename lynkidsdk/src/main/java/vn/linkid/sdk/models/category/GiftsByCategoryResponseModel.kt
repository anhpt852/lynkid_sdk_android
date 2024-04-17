package vn.linkid.sdk.models.category

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

data class GiftInfo(
    val code: String?,
    val name: String?,
    val description: String?,
    val introduce: String?,
    val fullGiftCategoryCode: String?,
    val brandName: String?,
    val thirdPartyBrandName: String?,
    val vendor: String?,
    val effectiveFrom: String?,
    val effectiveTo: String?,
    val requiredCoin: Int?,
    val status: String?,
    val totalQuantity: Int?,
    val usedQuantity: Int?,
    val remainingQuantity: Int?,
    val fullPrice: Double?,
    val discountPrice: Double?,
    val isEGift: Boolean?,
    val targetAudience: TargetAudience?,
    val targetAudienceId: Int?,
    val lastModificationTime: String?,
    val creationTime: String?,
    val createdByUser: String?,
    val updatedByUser: String?,
    val tag: String?,
    val id: Int?,
    val office: List<Office>?,
    val totalWish: Int?,
    val giftGroups: List<GiftGroup>?,
    val vendorHotline: String?,
    val totalRedeem: Int?,
    val giftUpdatedTime: String?,
    val giftCreatedTime: String?,
    val vendorName: String?,
    val vendorType: String?,
    val vendorImage: String?,
    val merchantId: Int?,
    val merchantName: String?,
    val merchantAvatar: String?,
    val merchantDescription: String?,
    val brandId: Int?
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
