package vn.linkid.sdk.models.category

import vn.linkid.sdk.models.my_reward.ImageLink


data class Category(
    val code: String?,
    val name: String?,
    val description: String?,
    val status: String?,
    val level: Int?,
    val parentCode: String?,
    val parentId: Int?,
    val fullLink: String?,
    val categoryTypeCode: String?,
    val imageLink: ImageLink?
)
