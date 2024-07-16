package vn.linkid.sdk.models.diamond

data class GetDiamondMemberInfoResponseModel(
    val success: Boolean?,
    val error: String?,
    val errorCode: String?,
    val memberInfor: DiamondMemberInfo
)