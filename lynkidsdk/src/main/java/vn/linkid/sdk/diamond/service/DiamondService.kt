package vn.linkid.sdk.diamond.service

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import vn.linkid.sdk.LynkiD_SDK
import vn.linkid.sdk.cache.MainCache
import vn.linkid.sdk.models.category.GiftFilterModel
import vn.linkid.sdk.models.category.GiftType
import vn.linkid.sdk.models.category.GiftsByCategoryResponseModel
import vn.linkid.sdk.models.category.HomeCategoryResponseModel
import vn.linkid.sdk.models.diamond.GetDiamondCategoryResponseModel
import vn.linkid.sdk.utils.APIEndpoints
import vn.linkid.sdk.utils.Endpoints
import vn.linkid.sdk.utils.generateCacheKey

class DiamondService(private val api: APIEndpoints) {
    suspend fun getDiamondCategories(diamondCateCode: String): Flow<Result<GetDiamondCategoryResponseModel>> =
        flow {
            val params: MutableMap<String, Any> = mutableMapOf(
                "MemberCode" to LynkiD_SDK.memberCode,
                "MaxLevelFilter" to 2,
                "MaxItem" to 5,
                "StatusFilter" to "A",
                "Sorting" to "CreationTime desc",
                "ShowTopupCategory" to false,
                "ParentCodeFilter" to diamondCateCode
            )
            val cacheKey = generateCacheKey(Endpoints.GET_DIAMOND_CATEGORIES, params)
            val cachedResponse = MainCache.get<GetDiamondCategoryResponseModel>(cacheKey)
            if (cachedResponse != null) {
                emit(Result.success(cachedResponse))
            } else {
                val response = api.getDiamondCategories(queries = params)
                MainCache.put(cacheKey, response)
                emit(Result.success(response))
            }
        }.catch {
            Log.e("DiamondService", "getDiamondCategories: ${it.message}")
            emit(Result.failure(RuntimeException("Something went wrong")))
        }


    suspend fun getGiftsByCategoryCode(
        categoryCode: String,
        parentCode: String?,
        index: Int,
        filter: GiftFilterModel
    ): Flow<Result<GiftsByCategoryResponseModel>> = flow {
        val params: MutableMap<String, Any> = mutableMapOf(
            "MemberCode" to LynkiD_SDK.memberCode,
            "GiftCategoryCodeFilter" to categoryCode,
            "SkipCount" to index * 10,
            "Sorting" to filter.sorting.id,
            "MaxItem" to 10
        )
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
            params["FullGiftCategoryCodeFilter"] = if (!parentCode.isNullOrEmpty()) {
                filter.selectedCates
                    .mapNotNull { it.code?.takeIf { id -> id.isNotEmpty() } }
                    .joinToString(separator = ";") { "${parentCode},${it}" }
            } else {
                filter.selectedCates
                    .mapNotNull { it.code?.takeIf { id -> id.isNotEmpty() } }
                    .joinToString(separator = ";")
            }
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
        Log.e("DiamondService", "getGiftsByCategoryCode: ${it.message}")
        emit(Result.failure(RuntimeException("Something went wrong")))
    }
}