package vn.linkid.sdk.models.merchant

data class AdditionalData(
    val id: Int?,
    val createdAt: String?,
    val updatedAt: String?,
    val isDeleted: Boolean?,
    val merchantId: Int?,
    val introduction: String?,
    val hotline: String?,
    val website: String?,
    val coverPhoto: String?,
    val earningDescription: String?,
    val showedUsers: Int?
)
