package vn.linkid.sdk.my_reward.repository

import android.util.Log
import kotlinx.coroutines.flow.map
import vn.linkid.sdk.my_reward.service.MyRewardDetailService

class MyRewardDetailRepository(private val service: MyRewardDetailService) {

    suspend fun getMyRewardDetail(transactionCode: String) =
        service.getMyRewardDetail(transactionCode).map { result ->
            if (result.isSuccess) {
                Result.success(result.getOrNull())
            } else {
                Log.d(
                    "MyRewardDetailRepository",
                    "getMyRewardDetail: ${result.exceptionOrNull()?.toString()}"
                )
                Result.failure(result.exceptionOrNull()!!)
            }
        }

}