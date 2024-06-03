package vn.linkid.sdk.my_reward.service

data class MyRewardFilterModel(
    val giftType: GiftType = GiftType.NONE,
    val expireType: ExpireType = ExpireType.NONE,
    val giftReceiveType: GiftReceiveType = GiftReceiveType.NONE
)

enum class GiftReceiveType(val id: String?, val title: String) {
    SENT("Sent", "Đã tặng"),
    RECEIVE("Receive", "Được tặng"),
    NONE(null, "");

    companion object {
        fun fromId(id: String?): GiftReceiveType {
            return when (id) {
                "Sent" -> SENT
                "Receive" -> RECEIVE
                else -> NONE
            }
        }
    }
}

enum class ExpireType(val id: String?, val title: String) {
    NEAR("None", "Sắp hết hạn"),
    USED("Used", "Đã sử dụng"),
    EXPIRED("Expired", "Hết hạn"),
    NONE(null, "");

    companion object {
        fun fromId(id: String?): ExpireType {
            return when (id) {
                "Used" -> USED
                "Expired" -> EXPIRED
                else -> NONE
            }
        }
    }
}

enum class GiftType(val id: String?, val title: String, val isEgift: Boolean?) {
    EGIFT("EGift", "Voucher", true),
    PHYSICAL_GIFT("PhysicalGift", "Quà hiện vật", false),
    NONE(null, "", null);

    companion object {
        fun fromId(id: String?): GiftType {
            return when (id) {
                "EGift" -> EGIFT
                "PhysicalGift" -> PHYSICAL_GIFT
                else -> NONE
            }
        }
    }
}

