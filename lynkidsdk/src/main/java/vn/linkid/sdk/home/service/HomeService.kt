package vn.linkid.sdk.home.service

import android.util.Log
import vn.linkid.sdk.utils.APIEndpoints
import vn.linkid.sdk.models.category.HomeCategoryResponseModel
import vn.linkid.sdk.models.banner.HomeNewsAndBannerModel
import vn.linkid.sdk.models.gift.HomeGiftGroupResponseModel
import vn.linkid.sdk.models.member.MemberResponseModel
import vn.linkid.sdk.models.point.PointResponseModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.Flow
import vn.linkid.sdk.LynkiD_SDK
import vn.linkid.sdk.utils.Endpoints
import vn.linkid.sdk.cache.MainCache
import vn.linkid.sdk.models.flash_sale.GetAllFlashSaleProgramResponseModel
import vn.linkid.sdk.utils.generateCacheKey

class HomeService(private val api: APIEndpoints) {

    suspend fun getMemberInfo(): Flow<Result<MemberResponseModel>> = flow {
        val cacheKey = Endpoints.GET_MEMBER_INFO
        val cachedResponse = MainCache.get<MemberResponseModel>(cacheKey)
        if (cachedResponse != null) {
            emit(Result.success(cachedResponse))
        } else {
            val response = api.getMemberInfo()
            MainCache.put(cacheKey, response)
            emit(Result.success(response))
        }
    }.catch {
        Log.e("HomeService", "getMemberInfo: ${it.message}")
        emit(Result.failure(RuntimeException("Something went wrong")))
    }

    suspend fun getPointInfo(): Flow<Result<PointResponseModel>> = flow {
        val cacheKey = Endpoints.GET_POINT_INFO
        val cachedResponse = MainCache.get<PointResponseModel>(cacheKey)
        if (cachedResponse != null) {
            emit(Result.success(cachedResponse))
        } else {
            val response = api.getPointInfo()
            MainCache.put(cacheKey, response)
            emit(Result.success(response))
        }
    }.catch {
        Log.e("HomeService", "getPointInfo: ${it.message}")
        emit(Result.failure(RuntimeException("Something went wrong")))
    }

    suspend fun getBannerAndNews(): Flow<Result<HomeNewsAndBannerModel>> = flow {
        val params: MutableMap<String, Any> = mutableMapOf(
            "SkipCount" to 0,
            "MaxResultCount" to 5,
            "Sorting" to "Article.Ordinal asc"
        )
        val cacheKey = generateCacheKey(Endpoints.GET_BANNER_AND_NEWS, params)
        val cachedResponse = MainCache.get<HomeNewsAndBannerModel>(cacheKey)
        if (cachedResponse != null) {
            emit(Result.success(cachedResponse))
        } else {
            val response = api.getBannerAndNews(queries = params)
            MainCache.put(cacheKey, response)
            emit(Result.success(response))
        }
    }.catch {
        Log.e("HomeService", "getBannerAndNews: ${it.message}")
        emit(Result.failure(RuntimeException("Something went wrong")))
    }

    suspend fun getHomeCategories(): Flow<Result<HomeCategoryResponseModel>> = flow {
        val cacheKey = Endpoints.GET_HOME_CATEGORIES
        val cachedResponse = MainCache.get<HomeCategoryResponseModel>(cacheKey)
        if (cachedResponse != null) {
            emit(Result.success(cachedResponse))
        } else {
            val response = api.getHomeCategories()
            MainCache.put(cacheKey, response)
            emit(Result.success(response))
        }
    }.catch {
        Log.e("HomeService", "getHomeCategories: ${it.message}")
        emit(Result.failure(RuntimeException("Something went wrong")))
    }

    suspend fun getHomeGiftGroup(): Flow<Result<HomeGiftGroupResponseModel>> = flow {
        val cacheKey = Endpoints.GET_HOME_GIFT_GROUP
        val cachedResponse = MainCache.get<HomeGiftGroupResponseModel>(cacheKey)
        if (cachedResponse != null) {
            emit(Result.success(cachedResponse))
        } else {
            val response = api.getHomeGiftGroup()
            MainCache.put(cacheKey, response)
            emit(Result.success(response))
        }
    }.catch {
        Log.e("HomeService", "getHomeGiftGroup: ${it.message}")
        emit(Result.failure(RuntimeException("Something went wrong")))
    }

    suspend fun getAllFlashSaleProgram(): Flow<Result<GetAllFlashSaleProgramResponseModel>> = flow {
        val params: MutableMap<String, Any> = mutableMapOf(
            "MemberCode" to LynkiD_SDK.memberCode,
            "MaxItemFlashSaleProgram" to 1,
            "MaxItemGiftFlashSale" to 0
        )
        val cacheKey = generateCacheKey(Endpoints.GET_ALL_FLASH_SALE_PROGRAM, params)
        val cachedResponse = MainCache.get<GetAllFlashSaleProgramResponseModel>(cacheKey)
        if (cachedResponse != null) {
            emit(Result.success(cachedResponse))
        } else {
            val response = api.getAllFlashSaleProgram(queries = params)
            MainCache.put(cacheKey, response)
            emit(Result.success(response))
        }
    }.catch {
        Log.e("HomeService", "getAllFlashSaleProgram: ${it.message}")
        emit(Result.failure(RuntimeException("Something went wrong")))
    }
}