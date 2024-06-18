package vn.linkid.sdk.search.service

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import vn.linkid.sdk.LynkiD_SDK
import vn.linkid.sdk.models.gift.AllGiftGroupResponseModel
import vn.linkid.sdk.utils.APIEndpoints

class SearchService(private val api: APIEndpoints) {

    suspend fun searchGift(
        keyword: String,
        index: Int
    ): Flow<Result<AllGiftGroupResponseModel>> = flow {
        emit(
            Result.success(
                api.searchGift(
                    queries = mutableMapOf(
                        "MemberCode" to LynkiD_SDK.memberCode,
                        "Filter" to keyword,
                        "SkipCount" to index * 10,
                        "MaxItem" to 10
                    )
                )
            )
        )
    }.catch {
        Log.e("SearchService", "searchGift: ${it.message}")
        emit(Result.failure(RuntimeException("Something went wrong")))
    }

}