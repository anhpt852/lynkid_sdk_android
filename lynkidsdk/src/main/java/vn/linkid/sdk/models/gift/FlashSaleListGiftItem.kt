import com.google.gson.annotations.SerializedName

data class FlashSaleListGiftItem(
    @SerializedName("giftId") val giftId: Int?,
    @SerializedName("giftCode") val giftCode: String?,
    @SerializedName("imageLink") val imageLink: String?,
    @SerializedName("giftName") val giftName: String?,
    @SerializedName("remainingQuantity") val remainingQuantity: Double?,
    @SerializedName("amount") val amount: Double?,
    @SerializedName("remainingQuantityFlashSale") val remainingQuantityFlashSale: Int?,
    @SerializedName("originalPrice") val originalPrice: Double?,
    @SerializedName("salePrice") val salePrice: Double?,
    @SerializedName("reductionRateDisplay") val reductionRateDisplay: Int?,
    @SerializedName("usedQuantityFlashSale") val usedQuantityFlashSale: Int?,
    @SerializedName("warningOutOfStock") val warningOutOfStock: Boolean?,
    @SerializedName("fullGiftCategoryCode") val fullGiftCategoryCode: String?,
    @SerializedName("totalWish") val totalWish: Int?
)
