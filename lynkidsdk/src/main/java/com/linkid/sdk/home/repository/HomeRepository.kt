package com.linkid.sdk.home.repository

import android.util.Log
import com.linkid.sdk.home.models.HomeNewsAndBannerModel
import com.linkid.sdk.home.models.Member
import com.linkid.sdk.home.models.MemberResponseModel
import com.linkid.sdk.home.models.Point
import com.linkid.sdk.home.models.PointResponseModel
import com.linkid.sdk.home.service.HomeService
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
}