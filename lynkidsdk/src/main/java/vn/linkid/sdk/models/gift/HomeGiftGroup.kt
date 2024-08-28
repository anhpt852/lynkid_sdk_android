package vn.linkid.sdk.models.gift

data class HomeGiftGroup(
    val giftGroup: GiftGroupInfo?,
    val gifts: List<Gift>?,
    val numberOfGifts: Int?
)
