package vn.linkid.sdk.models.my_reward

import vn.linkid.sdk.models.gift.FlashSaleProgramInfor
import vn.linkid.sdk.models.gift.GiftInfo

enum class WhyHaveRewardType(val value: String) {
    SENT("SENT"),
    BOUGHT("BOUGHT"),
    RECEIVED("RECEIVED")
}

enum class RewardUsedStatus(val status: String) {
    USED("U"),
    EXPIRED("E"),
    REDEEMED("R")
}

data class GiftInfoItem(
    val giftTransaction: GiftTransaction?,
    val eGift: EGift?,
    val vendorInfo: VendorInfo?,
    val giftInfor: GiftInfo?,
    val imageLinks: List<ImageLink>?,
    val brandInfo: BrandInfo?,
    val thirdPartyCategoryName: String?,
    val flashSaleProgramInfor: FlashSaleProgramInfor?,
    val giftDiscountInfor: GiftDiscountInfor?,
    val memberNameFrom: String?,
    val memberImageLinkFrom: String?,
    val isMaster: Boolean?,
    val isAvailableToRedeemAgain: Boolean?,
    val feeInfor: String?,
    val giftCategoryTypeCode: String?,
    val balanceAbleToCashout: Int?,
    val giftUsageAddress: List<GiftAddressItem>?,
)

data class GiftTransaction(
    val code: String?,
    val buyerCode: String?,
    val ownerCode: String?,
    val transferTime: String?,
    val memberName: String?,
    val introduce: String?,
    val tag: String?,
    val phone: String?,
    val address: String?,
    val giftCode: String?,
    val giftName: String?,
    val name: String?,
    val quantity: Int?,
    val coin: Int?,
    val date: String?,
    val status: String?,
    val whyHaveIt: WhyHaveRewardType?,
    var rewardStatus: RewardStatus?,
    val memberId: Int?,
    val giftId: Int?,
    val reason: String?,
    val totalCoin: Double?,
    val description: String?,
    val lastModificationTime: String?,
    val linkAvatar: String?,
    val transactionCode: String?,
    val qrCode: String?,
    val codeDisplay: String?,
    val id: Int?,
    val linkShippingInfo: String?,
    val serialNo: String?,
    val recipientPhone: String?,
    val contactEmail: String?,
    val contactHotline: String?,
    val condition: String?,
    val giftDescription: String?,
    val eGiftUsedAt: String?,
    val rejectReason: String?,
    val isExperienceGift: Boolean?
) {
    init {
        rewardStatus = status?.let { status ->
            RewardStatus.entries.find { it.value == status }
        }
    }
}

enum class RewardStatus(val value: String) {
    PENDING("pending"),       // 1-1
    WAITING("waiting"),       // 1-1
    DELIVERING("delivering"), // 1-2
    DELIVERED("delivered"),   // 1-3
    RETURNING("returning"),   // 3-3
    RETURNED("returned"),     // 3-4
    CANCELLING("cancelling"), // 3-4
    CANCELED("canceled"),     // 3-4
    CONFIRM_FAILED("confirmFailed"), // 1-1
    CONFIRMED("confirmed"),   // 1-1
    REJECTED("rejected"),     // 2-2
    APPROVED("approved");     // 1-1
}

data class EGift(
    val type: String?,
    val code: String?,
    val description: String?,
    val status: String?,
    val usedStatus: RewardUsedStatus?,
    val expiredDate: String?,
    val giftCode: String?,
    val giftId: Int?,
    val lastModificationTime: String?,
    val creationTime: String?,
    val usageCheck: Boolean?,
)

data class VendorInfo(
    val image: String?,
    val hotLine: String?,
    val id: Int?,
    val type: String?,
    val vendorName: String?,
)

data class BrandInfo(
    val brandId: Int?,
    val brandName: String?,
    val brandImage: String?,
    val isBrandFavourite: Boolean?,
)

data class MerchantInfo(
    val merchantId: Int?,
    val merchantName: String?,
    val merchantAvatar: String?,
    val merchantDescription: String?,
)

data class GiftUsageAddress(
    val id: Int?,
    val name: String?,
    val address: String?,
    val phone: String?,
    val latitude: String?,
    val longitude: String?,
)

data class ImageLink(
    val code: String?,
    val type: String?,
    val link: String?,
    val isActive: Boolean?,
    val ordinal: Int?,
    val fullLink: String?,
    val id: Int?,
)

data class GiftDiscountInfor(
    val salePrice: Double?,
    val remainingQuantityFlashSale: Int?,
    val reductionRateDisplay: Int?,
    val warningOutOfStock: Boolean?,
    val status: String?,
    val redeemGiftQuantity: Int?,
    val redeemFlashSaleQuantity: Int?,
    val maxAmountRedeem: Int?,
)

data class GiftAddressItem(
    val id: Int?,
    val name: String?,
    val address: String?,
    val phone: String?,
    val latitude: String?,
    val longitude: String?,
)




