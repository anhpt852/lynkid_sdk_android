package vn.linkid.sdk.models.imedia

data class GetThirdPartyBrandByVendor(
    val id: Int?,
    val code: String?,
    val brandId: Int?,
    val brandName: String?,
    val vendorId: Int?,
    val thirdPartyBrandId: String?,
    val thirdPartyBrandName: String?,
    val isActive: Boolean?,
    val linkLogo: String?,
    val commissPercent: Int?
)
