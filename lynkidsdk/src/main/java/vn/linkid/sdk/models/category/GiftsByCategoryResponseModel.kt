package vn.linkid.sdk.models.category

import vn.linkid.sdk.models.gift.FlashSaleProgramInfor
import vn.linkid.sdk.models.gift.GiftInfo
import vn.linkid.sdk.models.my_reward.GiftDiscountInfor
import vn.linkid.sdk.models.my_reward.ImageLink

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
    val flashSaleProgramInfor: FlashSaleProgramInfor?,
    val giftDiscountInfor: GiftDiscountInfor?
)