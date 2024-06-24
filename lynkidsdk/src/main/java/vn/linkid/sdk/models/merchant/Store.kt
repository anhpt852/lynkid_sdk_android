package vn.linkid.sdk.models.merchant

data class Store(
    val id: Int?,
    val merchantId: Int?,
    val storeName: String?,
    val unsignName: String?,
    val phoneNumber: String?,
    val address: String?,
    val email: String?,
    val status: String?,
    val avatar: String?,
    val region: String?,
    val longitude: Double?,
    val latitude: Double?,
    val description: String?
)
