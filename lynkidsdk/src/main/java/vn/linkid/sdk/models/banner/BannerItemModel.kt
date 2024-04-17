package vn.linkid.sdk.models.banner

import vn.linkid.sdk.models.article.Article

data class BannerItemModel(
    val article: Article?,
    val createdByUser: Any?,
    val relatedNews: List<Any>?
)