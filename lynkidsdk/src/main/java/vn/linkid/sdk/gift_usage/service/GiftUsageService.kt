package vn.linkid.sdk.gift_usage.service

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import vn.linkid.sdk.LynkiD_SDK
import vn.linkid.sdk.cache.MainCache
import vn.linkid.sdk.models.gift_usage.GiftUsageResponseModel
import vn.linkid.sdk.utils.APIEndpoints
import vn.linkid.sdk.utils.Endpoints
import vn.linkid.sdk.utils.generateCacheKey

class GiftUsageService(private val api: APIEndpoints) {

    suspend fun getGiftUsage(
        index: Int,
        giftCode: String,
        name: String?,
    ): Flow<Result<GiftUsageResponseModel>> = flow {
        val params: MutableMap<String, Any> = mutableMapOf(
            "SkipCount" to index,
            "MaxResultCount" to 10,
            "GiftCode" to giftCode
        )
        if (!name.isNullOrEmpty()) {
            params["Name"] = name
        }
        val cacheKey = generateCacheKey(Endpoints.GET_GIFT_USAGE_ADDRESS, params)
        val cachedResponse = MainCache.get<GiftUsageResponseModel>(cacheKey)
        if (cachedResponse != null) {
            emit(Result.success(cachedResponse))
        } else {
            val response = api.getGiftUsageAddress(queries = params)
            MainCache.put(cacheKey, response)
            emit(Result.success(response))
        }
    }.catch {
        Log.e("GiftUsageService", "getGiftUsage: ${it.message}")
        emit(Result.failure(RuntimeException("Something went wrong")))
    }

}