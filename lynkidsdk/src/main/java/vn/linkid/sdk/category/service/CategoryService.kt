package vn.linkid.sdk.category.service

import android.util.Log
import vn.linkid.sdk.APIEndpoints
import vn.linkid.sdk.LynkiD_SDK
import vn.linkid.sdk.models.category.GiftCategoryResponseModel
import vn.linkid.sdk.models.category.GiftsByCategoryResponseModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class CategoryService(private val api: APIEndpoints) {
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
        Log.e("CategoryService", "getGiftCategories: ${it.message}")
        emit(Result.failure(RuntimeException("Something went wrong")))
    }
    suspend fun getGiftsByCategoryCode(
        categoryCode: String,
        index: Int
    ): Flow<Result<GiftsByCategoryResponseModel>> = flow {
        emit(
            Result.success(
                api.getGiftsByCategory(
                    queries = mutableMapOf(
                        "MemberCode" to vn.linkid.sdk.LynkiD_SDK.memberCode,
                        "GiftCategoryCodeFilter" to categoryCode,
                        "SkipCount" to index,
                        "MaxItem" to 50
                    )
                )
            )
        )
    }.catch {
        Log.e("CategoryService", "getGiftsByCategoryCode: ${it.message}")
        emit(Result.failure(RuntimeException("Something went wrong")))
    }
}