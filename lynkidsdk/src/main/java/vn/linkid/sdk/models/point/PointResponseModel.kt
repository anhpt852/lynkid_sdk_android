package vn.linkid.sdk.models.point

import vn.linkid.sdk.models.BaseModel

data class PointResponseModel(
    val data: PointResponse?,
    val message: String?,
    val isSuccess: Boolean?
)

data class PointResponse(
    val result: Int?,
    val items: Point?
) : BaseModel()