package vn.linkid.sdk.models.transaction

import vn.linkid.sdk.models.my_reward.BrandInfo
import vn.linkid.sdk.models.my_reward.EGift
import vn.linkid.sdk.models.my_reward.GiftUsageAddress
import vn.linkid.sdk.models.my_reward.ImageLink
import vn.linkid.sdk.models.my_reward.MerchantInfo
import vn.linkid.sdk.models.my_reward.VendorInfo

data class TransactionItem(
    val giftTransaction: GiftTransaction?,
    val eGift: EGift?,
    val vendorInfo: VendorInfo?,
    val imageLinks: List<ImageLink>?,
    val merchantInfo: MerchantInfo?,
    val brandInfo: BrandInfo?,
    val remainingQuantityOfTheGift: Int?,
    val urBoxHaveSerial: Boolean?,
    val transactionStatus: String?,
    val thirdPartyCategoryName: String?,
    val memberNameFrom: String?,
    val memberImageLinkFrom: String?,
    val isMaster: Boolean?,
    val isAvailableToRedeemAgain: Boolean?,
    val giftUsageAddress: List<GiftUsageAddress>?,
)