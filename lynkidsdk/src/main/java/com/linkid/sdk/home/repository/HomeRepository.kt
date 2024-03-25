package com.linkid.sdk.home.repository

import android.util.Log
import com.linkid.sdk.models.category.Category
import com.linkid.sdk.models.category.HomeCategoryResponseModel
import com.linkid.sdk.models.banner.HomeNewsAndBannerModel
import com.linkid.sdk.models.member.Member
import com.linkid.sdk.models.member.MemberResponseModel
import com.linkid.sdk.models.point.Point
import com.linkid.sdk.models.point.PointResponseModel
import com.linkid.sdk.home.service.HomeService
import com.linkid.sdk.models.gift.HomeGiftGroupResponseModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class HomeRepository(private val service: HomeService) {

    suspend fun getMemberInfo(): Flow<Result<Member>> =
        service.getMemberInfo().map { result ->
            if (result.isSuccess) {
                val memberResponseModel: MemberResponseModel? = result.getOrNull()
                if (memberResponseModel != null && memberResponseModel.result == 200 && memberResponseModel.items != null) {
                    Result.success(memberResponseModel.items)
                } else {
                    Result.failure(result.exceptionOrNull()!!)
                }
            } else {
                Log.d("HomeRepository", "getMemberInfo: ${result.exceptionOrNull()?.toString()}")
                Result.failure(result.exceptionOrNull()!!)
            }
        }

    suspend fun getPointInfo(): Flow<Result<Point>> =
        service.getPointInfo().map { result ->
            if (result.isSuccess) {
                val pointResponseModel: PointResponseModel? = result.getOrNull()
                if (pointResponseModel != null && pointResponseModel.result == 200 && pointResponseModel.items != null) {
                    Result.success(pointResponseModel.items)
                } else {
                    Result.failure(result.exceptionOrNull()!!)
                }
            } else {
                Log.d("HomeRepository", "getPointInfo: ${result.exceptionOrNull()?.toString()}")
                Result.failure(result.exceptionOrNull()!!)
            }
        }

    suspend fun getBannerAndNews(): Flow<Result<HomeNewsAndBannerModel>> =
        service.getBannerAndNews().map { result ->
            if (result.isSuccess) {
                Result.success(result.getOrNull()!!)
            } else {
                Log.d("HomeRepository", "getBannerAndNews: ${result.exceptionOrNull()?.toString()}")
                Result.failure(result.exceptionOrNull()!!)
            }
        }

    suspend fun getHomeCategories(): Flow<Result<List<Category>>> =
        service.getHomeCategories().map { result ->
            if (result.isSuccess) {
                val homeCategoryResponseModel: HomeCategoryResponseModel? = result.getOrNull()
                if (homeCategoryResponseModel?.data?.row2 != null) {
                    Result.success(homeCategoryResponseModel.data.row2)
                } else {
                    Result.failure(result.exceptionOrNull()!!)
                }
            } else {
                Log.d(
                    "HomeRepository",
                    "getHomeCategories: ${result.exceptionOrNull()?.toString()}"
                )
                Result.failure(result.exceptionOrNull()!!)
            }
        }

    suspend fun getHomeGiftGroup(): Flow<Result<HomeGiftGroupResponseModel>> =
        service.getHomeGiftGroup().map { result ->
            if (result.isSuccess) {
                val homeGiftGroupResponseModel: HomeGiftGroupResponseModel? = result.getOrNull()
                if (homeGiftGroupResponseModel != null) {
                    Result.success(homeGiftGroupResponseModel)
                } else {
                    Result.failure(result.exceptionOrNull()!!)
                }
            } else {
                Log.d(
                    "HomeRepository",
                    "getHomeGiftGroup: ${result.exceptionOrNull()?.toString()}"
                )
                Result.failure(result.exceptionOrNull()!!)
            }
        }
}