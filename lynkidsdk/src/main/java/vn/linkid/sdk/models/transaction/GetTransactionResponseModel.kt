package vn.linkid.sdk.models.transaction

data class GetTransactionResponseModel(
    val data: GetTransaction?,
    val message: String?,
    val isSuccess: Boolean?
)
