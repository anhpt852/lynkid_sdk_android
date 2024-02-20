package com.linkid.sdk.all_gift.service

import android.util.Log
import com.linkid.sdk.APIEndpoints
import com.linkid.sdk.LynkiD_SDK
import com.linkid.sdk.models.category.GiftCategoryResponseModel
import com.linkid.sdk.models.gift.HomeGiftGroupResponseModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class AllGiftService(private val api: APIEndpoints){
    suspend fun getGiftCategories(): Flow<Result<GiftCategoryResponseModel>> = flow {
        emit(
            Result.success(
                api.getGiftCategories(
                    queries = mutableMapOf(
                        "memberCode" to LynkiD_SDK.memberCode
                    )
                )
            )
        )
    }.catch {
        Log.e("AllGiftService", "getGiftCategories: ${it.message}")
        emit(Result.failure(RuntimeException("Something went wrong")))
    }

    suspend fun getAllGiftGroups(): Flow<Result<HomeGiftGroupResponseModel>> = flow {
        emit(
            Result.success(
                api.getAllGiftGroups(
                    queries = mutableMapOf(
                        "memberCode" to LynkiD_SDK.memberCode
                    )
                )
            )
        )
    }.catch {
        Log.e("AllGiftService", "getAllGiftGroups: ${it.message}")
        emit(Result.failure(RuntimeException("Something went wrong")))
    }
}