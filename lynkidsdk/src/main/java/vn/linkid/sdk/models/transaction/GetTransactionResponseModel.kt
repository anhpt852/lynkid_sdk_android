package vn.linkid.sdk.models.transaction

data class GetTransactionResponseModel(
    val result: Int?,
    val items: List<GetTransactionItem>?,
    val message: String?,
    val totalCount: Int?
)
