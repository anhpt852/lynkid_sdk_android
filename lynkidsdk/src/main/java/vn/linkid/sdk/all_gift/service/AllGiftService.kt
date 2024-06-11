package vn.linkid.sdk.all_gift.service

import android.util.Log
import vn.linkid.sdk.utils.APIEndpoints
import vn.linkid.sdk.LynkiD_SDK
import vn.linkid.sdk.models.gift.AllGiftGroupResponseModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import vn.linkid.sdk.models.category.HomeCategoryResponseModel

class AllGiftService(private val api: APIEndpoints) {
    suspend fun getGiftCategories(): Flow<Result<HomeCategoryResponseModel>> = flow {
        emit(
            Result.success(
                api.getHomeCategories()
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
                        "MemberCode" to LynkiD_SDK.memberCode,
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