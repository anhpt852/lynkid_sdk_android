package com.linkid.sdk.home.service

import android.util.Log
import com.linkid.sdk.APIEndpoints
import com.linkid.sdk.home.models.HomeNewsAndBannerModel
import com.linkid.sdk.home.models.MemberResponseModel
import com.linkid.sdk.home.models.PointResponseModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.Flow

class HomeService(private val api: APIEndpoints) {

    suspend fun getMemberInfo(): Flow<Result<MemberResponseModel>> = flow {
        emit(
            Result.success(
                api.getMemberInfo()
            )
        )
    }.catch {
        Log.e("HomeService", "getMemberInfo: ${it.message}")
        emit(Result.failure(RuntimeException("Something went wrong")))
    }

    suspend fun getPointInfo(): Flow<Result<PointResponseModel>> = flow {
        emit(
            Result.success(
                api.getPointInfo()
            )
        )
    }.catch {
        Log.e("HomeService", "getPointInfo: ${it.message}")
        emit(Result.failure(RuntimeException("Something went wrong")))
    }

    suspend fun getBannerAndNews(): Flow<Result<HomeNewsAndBannerModel>> = flow {
        emit(
            Result.success(
                api.getBannerAndNews(
                    queries = mutableMapOf(
                        "SkipCount" to 0, "MaxResultCount" to 5, "Sorting" to "Article.Ordinal asc"
                    )
                )
            )
        )
    }.catch {
        Log.e("HomeService", "getBannerAndNews: ${it.message}")
        emit(Result.failure(RuntimeException("Something went wrong")))
    }
}