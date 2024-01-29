package com.linkid.sdk.models.point

import com.linkid.sdk.BaseModel

data class PointResponseModel(
    val result: Int?,
    val items: Point?
) : BaseModel()