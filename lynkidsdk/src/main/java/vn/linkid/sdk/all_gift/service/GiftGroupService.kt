package vn.linkid.sdk.all_gift.service

import android.util.Log
import vn.linkid.sdk.utils.APIEndpoints
import vn.linkid.sdk.LynkiD_SDK
import vn.linkid.sdk.models.gift.AllGiftGroupResponseModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import vn.linkid.sdk.cache.MainCache
import vn.linkid.sdk.models.category.GiftFilterModel
import vn.linkid.sdk.models.category.GiftType
import vn.linkid.sdk.models.category.GiftsByCategoryResponseModel
import vn.linkid.sdk.models.category.HomeCategoryResponseModel
import vn.linkid.sdk.utils.Endpoints
import vn.linkid.sdk.utils.generateCacheKey

class GiftGroupService(private val api: APIEndpoints) {

    suspend fun getGiftsByGroupCode(
        groupCode: String,
        index: Int
    ): Flow<Result<GiftsByCategoryResponseModel>> = flow {
        val params: MutableMap<String, Any> = mutableMapOf(
            "MemberCode" to LynkiD_SDK.memberCode,
            "SkipCount" to index * 10,
            "Sorting" to "LastModificationTime desc",
            "MaxResultCount" to 10,
            "StatusFilter" to "A",
            "MaxRequiredCoinFilter" to 1000000000,
            "GiftGroupCodeFilter" to groupCode
        )
        val cacheKey = generateCacheKey(Endpoints.GET_GIFTS_BY_GROUP, params)
        val cachedResponse = MainCache.get<GiftsByCategoryResponseModel>(cacheKey)
        if (cachedResponse != null) {
            emit(Result.success(cachedResponse))
        } else {
            val response = api.getGiftsByGroup(queries = params)
            MainCache.put(cacheKey, response)
            emit(Result.success(response))
        }
    }.catch {
        Log.e("GiftGroupService", "getGiftsByGroupCode: ${it.message}")
        emit(Result.failure(RuntimeException("Something went wrong")))
    }
}