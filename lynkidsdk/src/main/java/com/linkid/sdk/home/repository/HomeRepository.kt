package com.linkid.sdk.home.repository

import com.linkid.sdk.home.models.HomeNewsAndBannerModel
import com.linkid.sdk.home.service.HomeService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class HomeRepository(private val service: HomeService) {
    suspend fun getBannerAndNews(): Flow<Result<HomeNewsAndBannerModel>> =
        service.getBannerAndNews().map { result ->
            if (result.isSuccess) {
//                Result.success(mapper.invoke(result.getOrNull()!!))
                Result.success(result.getOrNull()!!)
            } else {
                Result.failure(result.exceptionOrNull()!!)
            }
        }
}