package vn.linkid.sdk.models.address

data class Address(
    val id: Int?,
    val code: String?,
    val name: String?,
    val description: String?,
    val parentCode: String?,
    val internalCode: String?,
    val level: String?
)
