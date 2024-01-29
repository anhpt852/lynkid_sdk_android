package com.linkid.sdk.models.article

data class Article(
    val id: Int?,
    val code: String?,
    val name: String?,
    val description: String?,
    val content: String?,
    val avatar: String?,
    val linkAvatar: String?,
    val status: String?,
    val fromDate: String?,
    val toDate: String?,
    val tags: String?,
    val updatedByUser: String?,
    val tenantId: String?,
    val requiredReadingTime: Int?,
    val rewardedCoin: Int?,
    val actionCode: String?,
    val ordinal: Int?,
    val creationTime: String?,
    val extraType: String?
)