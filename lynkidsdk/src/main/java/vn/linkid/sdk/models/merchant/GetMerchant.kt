package vn.linkid.sdk.models.merchant

data class GetMerchant(
    val walletAddress: String?,
    val id: Int?,
    val merchantName: String?,
    val address: String?,
    val phone: String?,
    val status: String?,
    val logo: String?,
    val type: String?,
    val baseUnit: Int?,
    val pointExchangeRate: Int?,
    val maintenanceFrom: String?,
    val maintenanceTo: String?,
    val maintenanceStatus: String?,
    val isAKCLoyalty: Boolean?,
    val x1lTenantId: String?,
    val orgId: String?,
    val coinIcon: String?,
    val allowConnectFromLynkId: Boolean?,
    val storeList: List<Store>?,
    val additionalData: AdditionalData?
)
