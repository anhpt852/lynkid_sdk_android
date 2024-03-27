package com.linkid.sdk.models.category

import com.linkid.sdk.BaseModel

data class HomeCategoryResponseModel(
    val data: HomeCategory,
    val message: String?,
    val isSuccess: Boolean?
)