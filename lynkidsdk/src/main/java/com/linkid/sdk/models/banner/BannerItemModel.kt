package com.linkid.sdk.models.banner

import com.linkid.sdk.models.article.Article

data class BannerItemModel(
    val article: Article?,
    val createdByUser: Any?,
    val relatedNews: List<Any>?
)