package vn.linkid.sdk.models.category

import vn.linkid.sdk.BaseModel

data class HomeCategoryResponseModel(
    val data: HomeCategory,
    val message: String?,
    val isSuccess: Boolean?
)