package vn.linkid.sdk.models.diamond

import vn.linkid.sdk.models.gift.GiftInfo
import vn.linkid.sdk.models.my_reward.ImageLink

data class DiamondGiftInfo(
    val giftInfor: GiftInfo?,
    val imageLink: ImageLink?
)