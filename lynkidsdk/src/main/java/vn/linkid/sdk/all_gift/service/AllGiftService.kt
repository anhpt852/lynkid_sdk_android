package vn.linkid.sdk.all_gift.service

import android.util.Log
import vn.linkid.sdk.utils.APIEndpoints
import vn.linkid.sdk.LynkiD_SDK
import vn.linkid.sdk.models.gift.AllGiftGroupResponseModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import vn.linkid.sdk.cache.MainCache
import vn.linkid.sdk.models.category.GiftsByCategoryResponseModel
import vn.linkid.sdk.models.category.HomeCategoryResponseModel
import vn.linkid.sdk.utils.Endpoints
import vn.linkid.sdk.utils.generateCacheKey

class AllGiftService(private val api: APIEndpoints) {
    suspend fun getGiftCategories(): Flow<Result<HomeCategoryResponseModel>> = flow {
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
        Log.e("AllGiftService", "getGiftCategories: ${it.message}")
        emit(Result.failure(RuntimeException("Something went wrong")))
    }

    suspend fun getAllGiftGroups(): Flow<Result<AllGiftGroupResponseModel>> = flow {
        val params: MutableMap<String, Any> = mutableMapOf(
            "MemberCode" to LynkiD_SDK.memberCode,
            "MaxItem" to 5,
            "MaxResultCount" to 10,
            "GiftGroupTypeFilter" to 0,
            "SimplifiedResponse" to true
        )
        val cacheKey = generateCacheKey(Endpoints.GET_ALL_GIFT_GROUPS, params)
        val cachedResponse = MainCache.get<AllGiftGroupResponseModel>(cacheKey)
        if (cachedResponse != null) {
            emit(Result.success(cachedResponse))
        } else {
            val response = api.getAllGiftGroups(queries = params)
            MainCache.put(cacheKey, response)
            emit(Result.success(response))
        }
    }.catch {
        Log.e("AllGiftService", "getAllGiftGroups: ${it.message}")
        emit(Result.failure(RuntimeException("Something went wrong")))
    }
}