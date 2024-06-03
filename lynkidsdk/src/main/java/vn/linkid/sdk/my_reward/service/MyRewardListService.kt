package vn.linkid.sdk.my_reward.service

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import vn.linkid.sdk.APIEndpoints
import vn.linkid.sdk.LynkiD_SDK
import vn.linkid.sdk.models.my_reward.MyRewardListResponseModel

class MyRewardListService(private val api: APIEndpoints) {

    suspend fun getMyRewards(
        page: Int = 0,
        filter: String,
        myRewardFilterModel: MyRewardFilterModel = MyRewardFilterModel()
    ): Flow<Result<MyRewardListResponseModel>> = flow {
        val queries = mutableMapOf<String, Any>(
            "OwnerCodeFilter" to LynkiD_SDK.memberCode,
            "MaxResultCount" to 10,
            "SkipCount" to page * 10,
            "Sorting" to "LastModificationTime desc"
        )

        myRewardFilterModel.giftType.id?.let { queries["TypeOfGift"] = it }
        myRewardFilterModel.giftReceiveType.id?.let { queries["TypeOfTransaction"] = it }
        when (myRewardFilterModel.expireType) {
            ExpireType.NEAR -> {
                queries["EgiftStatusFilter"] = filter
                queries["IsNearExpire"] = true
            }

            ExpireType.USED -> queries["EgiftStatusFilter"] = "U"
            ExpireType.EXPIRED -> queries["EgiftStatusFilter"] = "E"
            else -> queries["EgiftStatusFilter"] = filter
        }
        val result = api.getMyRewards(queries = queries)
        Log.d("MyRewardListService", "getMyRewards: $result")
        emit(Result.success(result))
    }.catch {
        Log.e("MyRewardListService", "getMyRewards: ${it.message}")
        emit(Result.failure(RuntimeException("Something went wrong")))
    }


}