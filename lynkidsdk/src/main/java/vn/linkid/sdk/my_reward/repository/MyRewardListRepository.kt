package vn.linkid.sdk.my_reward.repository

import android.util.Log
import kotlinx.coroutines.flow.map
import vn.linkid.sdk.models.my_reward.MyRewardListResponseModel
import vn.linkid.sdk.my_reward.service.MyRewardListService

class MyRewardListRepository(private val service: MyRewardListService) {

    suspend fun getMyRewards(page: Int, tab: Int) =
        service.getMyRewards(page, if (tab == 0) "R" else "U;E").map { result ->
            if (result.isSuccess) {
                val myRewardListResponseModel: MyRewardListResponseModel? = result.getOrNull()
                if (myRewardListResponseModel?.result != null) {
                    Result.success(myRewardListResponseModel.result)
                } else {
                    Result.failure(
                        result.exceptionOrNull() ?: RuntimeException("Something went wrong")
                    )
                }
            } else {
                Log.d(
                    "MyRewardRepository",
                    "getMyRewards: ${result.exceptionOrNull()?.message}"
                )
                Result.failure(RuntimeException("Something went wrong"))
            }
        }

}