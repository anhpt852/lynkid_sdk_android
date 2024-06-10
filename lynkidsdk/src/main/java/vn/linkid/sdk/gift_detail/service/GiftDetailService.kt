package vn.linkid.sdk.gift_detail.service

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import vn.linkid.sdk.APIEndpoints
import vn.linkid.sdk.LynkiD_SDK
import vn.linkid.sdk.gift_detail.cache.GiftDetailCache
import vn.linkid.sdk.models.gift.GiftDetailResponseModel

class GiftDetailService(private val api: APIEndpoints) {

    suspend fun getGiftDetail(id: Int): Flow<Result<GiftDetailResponseModel>> = flow {
        val cached = GiftDetailCache.get(id)
        if (cached != null) {
            emit(Result.success(cached))
        } else {
            val response = api.getGiftDetails(
                queries = mutableMapOf(
                    "MemberCode" to LynkiD_SDK.memberCode,
                    "GiftId" to id,
                    "MaxItem" to 1
                )
            )
            GiftDetailCache.put(id, response)  // Cache the new response
            emit(Result.success(response))
        }
    }.catch {
        Log.e("GiftDetailService", "getGiftDetail: ${it.message}")
        emit(Result.failure(RuntimeException("Something went wrong")))
    }

}