package vn.linkid.sdk.category.service

import android.util.Log
import vn.linkid.sdk.utils.APIEndpoints
import vn.linkid.sdk.LynkiD_SDK
import vn.linkid.sdk.models.category.GiftsByCategoryResponseModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import vn.linkid.sdk.cache.MainCache
import vn.linkid.sdk.models.category.GiftFilterModel
import vn.linkid.sdk.models.category.GiftType
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
        filter: GiftFilterModel
    ): Flow<Result<GiftsByCategoryResponseModel>> = flow {
        val params: MutableMap<String, Any> = mutableMapOf(
            "MemberCode" to LynkiD_SDK.memberCode,
            "SkipCount" to index * 10,
            "Sorting" to filter.sorting.id,
            "MaxItem" to 10
        )
        if(categoryCode.isNotEmpty() && categoryCode != "all") {
            params["GiftCategoryCodeFilter"] = categoryCode
        }
        if (filter.giftType != GiftType.NONE) {
            params["IsEGiftFilter"] = filter.giftType == GiftType.EGIFT
        }
        if (filter.fromCoin.isNotEmpty()) {
            params["FromCointFilter"] = filter.fromCoin
        }
        if (filter.toCoin.isNotEmpty()) {
            params["ToCoinFilter"] = filter.toCoin
        }
        if (filter.selectedCities.isNotEmpty()) {
            params["RegionCodeFilter"] = filter.selectedCities
                .mapNotNull { city -> city.optionId?.takeIf { it.isNotEmpty() } }
                .joinToString(separator = ";")
        }
        if (filter.searchText.isNotEmpty()) {
            params["Filter"] = filter.searchText
        }
        if (filter.selectedCates.isNotEmpty()) {
            params["FullGiftCategoryCodeFilter"] = filter.selectedCates
                .mapNotNull { city -> city.code?.takeIf { it.isNotEmpty() } }
                .joinToString(separator = ";")
        }
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