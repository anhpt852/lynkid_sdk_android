package vn.linkid.sdk.models.exchange

data class ExchangeResponseModel(
    val data: ExchangedData?,
    val isSuccess: Boolean?,
    val message: String?
)

data class ExchangedData(
    val items: List<ExchangedItem>?,
    val totalCount: Int?,
    val isOtpSent: Boolean?
)

data class ExchangedItem(
    val code: String?,
    val totalCoin: Double?,
    val status: String?,
    val date: String,
    val description: String?,
    val eGift: ExchangedEGift?
)

data class ExchangedEGift(
    val code: String?,
    val type: String?,
    val description: String?,
    val status: String?,
    val usedStatus: String?,
    val expiredDate: String?,
    val qrCode: String?
)
