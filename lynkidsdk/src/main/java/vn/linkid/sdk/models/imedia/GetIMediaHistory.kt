package vn.linkid.sdk.models.imedia

import vn.linkid.sdk.models.my_reward.BrandInfo

data class GetIMediaHistory(
    val id: Int?,
    val code: String?,
    val eGiftCode: String?,
    val eGiftStatus: String?,
    val eGiftExpiredDate: String?,
    val serialNo: String?,
    val giftName: String?,
    val thirdPartyBrandId: String?,
    val creationTime: String?,
    val vendorId: Int?,
    val requiredCoin: Int?,
    val transactionCode: String?,
    val thirdPartyCategoryName: String?,
    val topupCard: String?,
    val categoryName: String?,
    val whyHaveIt: String?,
    val status: String?,
    val recipientPhone: String?,
    val isTopup: Boolean?,
    val creationDateTime: String?,
    val fullPrice: Int?,
    val brandInfo: BrandInfo?
)
