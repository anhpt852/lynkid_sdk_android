package vn.linkid.sdk.all_gift.service

import android.util.Log
import vn.linkid.sdk.APIEndpoints
import vn.linkid.sdk.LynkiD_SDK
import vn.linkid.sdk.models.category.GiftCategoryResponseModel
import vn.linkid.sdk.models.gift.AllGiftGroupResponseModel
import vn.linkid.sdk.models.gift.HomeGiftGroupResponseModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class AllGiftService(private val api: APIEndpoints) {
    suspend fun getGiftCategories(): Flow<Result<GiftCategoryResponseModel>> = flow {
        emit(
            Result.success(
                api.getGiftCategories(
                    queries = mutableMapOf(
                        "MemberCode" to vn.linkid.sdk.LynkiD_SDK.memberCode,
                        "MaxLevelFilter" to 1,
                        "MaxItem" to 50,
                        "StatusFilter" to "A",
                        "Channel" to "linkid",
                        "Ver" to "next"
                    )
                )
            )
        )
    }.catch {
        Log.e("AllGiftService", "getGiftCategories: ${it.message}")
        emit(Result.failure(RuntimeException("Something went wrong")))
    }

    suspend fun getAllGiftGroups(): Flow<Result<AllGiftGroupResponseModel>> = flow {
        emit(
            Result.success(
                api.getAllGiftGroups(
                    queries = mutableMapOf(
                        "MemberCode" to vn.linkid.sdk.LynkiD_SDK.memberCode,
                        "MaxItem" to 5,
                        "MaxResultCount" to 10,
                        "GiftGroupTypeFilter" to 0,
                        "SimplifiedResponse" to true
                    )
                )
            )
        )
    }.catch {
        Log.e("AllGiftService", "getAllGiftGroups: ${it.message}")
        emit(Result.failure(RuntimeException("Something went wrong")))
    }
}