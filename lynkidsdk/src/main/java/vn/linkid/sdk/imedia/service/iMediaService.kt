package vn.linkid.sdk.imedia.service

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import vn.linkid.sdk.LynkiD_SDK
import vn.linkid.sdk.cache.MainCache
import vn.linkid.sdk.models.gift.GiftGroupResponseModel
import vn.linkid.sdk.utils.APIEndpoints
import vn.linkid.sdk.utils.Endpoints
import vn.linkid.sdk.utils.generateCacheKey

class iMediaService(private val api: APIEndpoints) {

    suspend fun getGiftsByGroupType(groupType: Int, sorting: String): Flow<Result<GiftGroupResponseModel>> = flow {
        val params: MutableMap<String, Any> = mutableMapOf(
            "MemberCode" to LynkiD_SDK.memberCode,
            "GroupTypeFilter" to groupType,
            "Sorting" to sorting
        )
        val cacheKey = generateCacheKey(Endpoints.GET_GIFTS_BY_GROUP_TYPE, params)
        val cachedResponse = MainCache.get<GiftGroupResponseModel>(cacheKey)
        if (cachedResponse != null) {
            emit(Result.success(cachedResponse))
        } else {
            val response = api.getGiftsByGroupType(queries = params)
            MainCache.put(cacheKey, response)
            emit(Result.success(response))
        }
    }.catch {
        Log.e("iMediaService", "getGiftsByGroupType: ${it.message}")
        emit(Result.failure(RuntimeException("Something went wrong")))
    }

}