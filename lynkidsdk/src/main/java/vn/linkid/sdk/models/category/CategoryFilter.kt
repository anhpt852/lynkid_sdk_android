package vn.linkid.sdk.models.category

// GiftType enum
enum class GiftType(val id: String?, val title: String, val isEgift: Boolean?) {
    EGIFT("EGIFT", "Voucher", true),
    PHYSICAL_GIFT("PHYSICAL_GIFT", "Quà hiện vật", false),
    NONE(null, "", null)
}

// GiftSorting enum
enum class GiftSorting(val id: String, val title: String) {
    POPULAR("totalRedeem", "Phổ biến nhất"),
    LATEST("giftCreatedTime", "Mới nhất"),
    TOTAL_WISH("TotalWish", ""),
    REQUIRED_COIN_ASC("priceLasted", ""),
    REQUIRED_COIN_DESC("priceLasted DESC", ""),
    PRICE_LASTED("PriceLasted", ""),
    DISPLAY_ORDER("displayOrder", "");

    companion object {
        const val DEFAULT_MAX_COIN = 20000000.0
    }
}

// Range class
data class Range(
    val fromCoin: String = "",
    val toCoin: String = "",
    val displayText: String = "",
    val id: String = ""
)

data class OptionModel(
    val optionId: String?,
    val optionTitle: String?
)

// GiftFilterModel class
data class GiftFilterModel(
    val giftType: GiftType = GiftType.NONE,
    val selectedCities: List<OptionModel> = emptyList(),
    val fromCoin: String = "",
    val toCoin: String = "",
    val isSuitablePrice: Boolean = false,
    val searchText: String = "",
    val selectedRange: Range = Range(),
    val selectedCates: List<Category> = emptyList(),
    val sorting: GiftSorting = GiftSorting.LATEST
)