package com.linkid.sdk.models.point

import com.linkid.sdk.BaseModel

data class PointResponseModel(
    val data: PointResponse?,
    val message: String?,
    val isSuccess: Boolean?
)

data class PointResponse(
    val result: Int?,
    val items: Point?
) : BaseModel()