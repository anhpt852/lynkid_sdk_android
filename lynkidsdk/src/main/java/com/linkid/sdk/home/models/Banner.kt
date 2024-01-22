package com.linkid.sdk.home.models

import com.linkid.sdk.BaseModel

data class HomeNewsAndBannerModel(
    val result: List<Result>?
) : BaseModel()

data class Result(
    val type: Int?,
    val resultInfo: BannerResultModel?
)

data class BannerResultModel(
    val totalCount: Int?,
    val items: List<BannerItemModel>?
)

data class BannerItemModel(
    val article: Article?,
    val createdByUser: Any?,
    val relatedNews: List<Any>?
)