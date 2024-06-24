package vn.linkid.sdk.models.merchant

data class GetMerchantResponseModel(
    val result: Int?,
    val items: List<GetMerchant>?,
    val message: String?,
    val totalCount: Int?
)
