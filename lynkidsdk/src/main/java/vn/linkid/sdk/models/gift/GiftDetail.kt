package vn.linkid.sdk.models.gift

import vn.linkid.sdk.models.my_reward.GiftAddressItem
import vn.linkid.sdk.models.my_reward.GiftDiscountInfor
import vn.linkid.sdk.models.my_reward.ImageLink

data class GiftDetail(
    val giftInfor: GiftInfo?,
    val imageLink: ImageLink?,
    val flashSaleProgramInfor: FlashSaleProgramInfor?,
    val giftDiscountInfor: GiftDiscountInfor?,
    val giftUsageAddress: List<GiftAddressItem>?,
    val errorCode: String?,
    val giftCategoryTypeCode: String?,
    val feeInfor: String?,
    val balanceAbleToCashout: Int?
)
