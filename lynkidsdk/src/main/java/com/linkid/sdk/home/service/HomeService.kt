package com.linkid.sdk.home.service

import com.linkid.sdk.APIEndpoints
import com.linkid.sdk.home.models.HomeNewsAndBannerModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.Flow

class HomeService(private val api: APIEndpoints) {
    suspend fun getBannerAndNews(): Flow<Result<HomeNewsAndBannerModel>> =
        flow {
            emit(
                Result.success(
                    api.getBannerAndNews(
                        queries = mapOf(
                            "SkipCount" to 0,
                            "MaxResultCount" to 5,
                            "Sorting" to "Article.Ordinal asc"
                        )
                    )
                )
            )
        }.catch {
            emit(Result.failure(RuntimeException("Something went wrong")))
        }
}