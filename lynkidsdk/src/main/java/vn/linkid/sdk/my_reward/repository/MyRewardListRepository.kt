package vn.linkid.sdk.my_reward.repository

import android.util.Log
import kotlinx.coroutines.flow.map
import vn.linkid.sdk.my_reward.service.MyRewardListService

class MyRewardListRepository(private val service: MyRewardListService) {

    suspend fun getMyRewards(index: Int) = service.getMyRewards(index).map { result ->
        if (result.isSuccess) {
            Result.success(result.getOrNull())
        } else {
            Log.d(
                "MyRewardRepository",
                "getMyRewards: ${result.exceptionOrNull()?.toString()}"
            )
            Result.failure(result.exceptionOrNull()!!)
        }
    }

}