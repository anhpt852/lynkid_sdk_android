package vn.linkid.sdk.my_reward.service

import android.util.Log
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import vn.linkid.sdk.APIEndpoints
import vn.linkid.sdk.LynkiD_SDK

class MyRewardListService(private val api: APIEndpoints) {

    suspend fun getMyRewards(index: Int) = flow {
        emit(
            Result.success(
                api.getMyRewards(queries = mutableMapOf(
                    "OwnerCodeFilter" to LynkiD_SDK.memberCode,
                    "SkipCount" to index,
                    "MaxResultCount" to 50,
                    "Sorting" to "LastModificationTime desc"
                ))
            )
        )
    }.catch {
        Log.e("MyRewardService", "getMyRewards: ${it.message}")
        emit(Result.failure(RuntimeException("Something went wrong")))
    }

}