package vn.linkid.sdk.category.service

import android.util.Log
import vn.linkid.sdk.utils.APIEndpoints
import vn.linkid.sdk.LynkiD_SDK
import vn.linkid.sdk.models.category.GiftsByCategoryResponseModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import vn.linkid.sdk.cache.MainCache
import vn.linkid.sdk.models.category.HomeCategoryResponseModel
import vn.linkid.sdk.utils.Endpoints
import vn.linkid.sdk.utils.generateCacheKey

class CategoryService(private val api: APIEndpoints) {
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
        Log.e("CategoryService", "getGiftCategories: ${it.message}")
        emit(Result.failure(RuntimeException("Something went wrong")))
    }

    suspend fun getGiftsByCategoryCode(
        categoryCode: String,
        index: Int,
    ): Flow<Result<GiftsByCategoryResponseModel>> = flow {
        val params: MutableMap<String, Any> = mutableMapOf(
            "MemberCode" to LynkiD_SDK.memberCode,
            "GiftCategoryCodeFilter" to categoryCode,
            "SkipCount" to index * 10,
            "MaxItem" to 10
        )
        val cacheKey = generateCacheKey(Endpoints.GET_GIFTS_BY_CATEGORY, params)
        val cachedResponse = MainCache.get<GiftsByCategoryResponseModel>(cacheKey)
        if (cachedResponse != null) {
            emit(Result.success(cachedResponse))
        } else {
            val response = api.getGiftsByCategory(queries = params)
            MainCache.put(cacheKey, response)
            emit(Result.success(response))
        }
    }.catch {
        Log.e("CategoryService", "getGiftsByCategoryCode: ${it.message}")
        emit(Result.failure(RuntimeException("Something went wrong")))
    }

    suspend fun getDiamondCategoryCode(
        categoryCode: String,
        index: Int,
    ): Flow<Result<GiftsByCategoryResponseModel>> = flow {
        val params: MutableMap<String, Any> = mutableMapOf(
            "MemberCode" to LynkiD_SDK.memberCode,
            "GiftCategoryCodeFilter" to categoryCode,
            "SkipCount" to index * 10,
            "MaxItem" to 10
        )
        val cacheKey = generateCacheKey(Endpoints.GET_GIFTS_BY_CATEGORY, params)
        val cachedResponse = MainCache.get<GiftsByCategoryResponseModel>(cacheKey)
        if (cachedResponse != null) {
            emit(Result.success(cachedResponse))
        } else {
            val response = api.getGiftsByCategory(queries = params)
            MainCache.put(cacheKey, response)
            emit(Result.success(response))
        }
    }.catch {
        Log.e("CategoryService", "getGiftsByCategoryCode: ${it.message}")
        emit(Result.failure(RuntimeException("Something went wrong")))
    }
}