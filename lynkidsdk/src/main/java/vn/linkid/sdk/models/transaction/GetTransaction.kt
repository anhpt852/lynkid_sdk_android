package vn.linkid.sdk.models.transaction

data class GetTransaction(
    val items: List<TransactionItem>?,
    val totalCount: Int?,
    val giftCategoryGiftCategoryName: String?
)