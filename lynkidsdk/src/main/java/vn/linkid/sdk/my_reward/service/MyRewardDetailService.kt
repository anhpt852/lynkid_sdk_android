package vn.linkid.sdk.my_reward.service

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import vn.linkid.sdk.utils.APIEndpoints
import vn.linkid.sdk.LynkiD_SDK
import vn.linkid.sdk.models.my_reward.MyRewardListResponseModel

class MyRewardDetailService(private val api: APIEndpoints) {
    suspend fun getMyRewardDetail(
        transactionCode: String
    ): Flow<Result<MyRewardListResponseModel>> = flow {
        val queries = mutableMapOf<String, Any>(
            "OwnerCodeFilter" to LynkiD_SDK.memberCode,
            "GiftTransactionCode" to transactionCode,
            "Ver" to "next"
        )
        val result = api.getMyRewards(queries = queries)
        Log.d("MyRewardDetailService", "getMyRewardDetail: $result")
        emit(Result.success(result))
    }.catch {
        Log.e("MyRewardDetailService", "getMyRewardDetail: ${it.message}")
        emit(Result.failure(RuntimeException("Something went wrong")))
    }
}