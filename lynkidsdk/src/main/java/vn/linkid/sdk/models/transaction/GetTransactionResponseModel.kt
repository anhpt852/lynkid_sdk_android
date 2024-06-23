package vn.linkid.sdk.models.transaction

data class GetTransactionResponseModel(
    val result: Int?,
    val items: List<GetTransactionDetailResponseModel>?,
    val message: String?,
    val totalCount: Int?
)
