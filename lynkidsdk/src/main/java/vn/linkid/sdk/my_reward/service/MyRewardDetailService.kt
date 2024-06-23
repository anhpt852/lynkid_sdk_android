package vn.linkid.sdk.my_reward.service

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import vn.linkid.sdk.utils.APIEndpoints
import vn.linkid.sdk.LynkiD_SDK
import vn.linkid.sdk.cache.MainCache
import vn.linkid.sdk.models.category.GiftsByCategoryResponseModel
import vn.linkid.sdk.models.my_reward.MyRewardListResponseModel
import vn.linkid.sdk.utils.Endpoints
import vn.linkid.sdk.utils.generateCacheKey

class MyRewardDetailService(private val api: APIEndpoints) {
    suspend fun getMyRewardDetail(
        transactionCode: String,
    ): Flow<Result<MyRewardListResponseModel>> = flow {
        val params: MutableMap<String, Any> = mutableMapOf(
            "OwnerCodeFilter" to LynkiD_SDK.memberCode,
            "GiftTransactionCode" to transactionCode,
            "Ver" to "next"
        )
        val cacheKey = generateCacheKey(Endpoints.GET_MY_REWARDS, params)
        val cachedResponse = MainCache.get<MyRewardListResponseModel>(cacheKey)
        if (cachedResponse != null) {
            emit(Result.success(cachedResponse))
        } else {
            val response = api.getMyRewards(queries = params)
            MainCache.put(cacheKey, response)
            emit(Result.success(response))
        }
    }.catch {
        Log.e("MyRewardDetailService", "getMyRewardDetail: ${it.message}")
        emit(Result.failure(RuntimeException("Something went wrong")))
    }
}