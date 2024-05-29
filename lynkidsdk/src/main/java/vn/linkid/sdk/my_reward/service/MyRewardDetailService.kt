package vn.linkid.sdk.my_reward.service

import kotlinx.coroutines.flow.flow
import vn.linkid.sdk.APIEndpoints
import vn.linkid.sdk.LynkiD_SDK

class MyRewardDetailService(private val api: APIEndpoints) {
    suspend fun getMyRewardDetail(transactionCode: String) =
        flow {
            emit(
                Result.success(
                    api.getMyRewardDetail(
                        queries = mutableMapOf(
                            "OwnerCodeFilter" to LynkiD_SDK.memberCode,
                            "GiftTransactionCode" to transactionCode,
                            "Ver" to "next"
                        )
                    )
                )
            )
        }
}