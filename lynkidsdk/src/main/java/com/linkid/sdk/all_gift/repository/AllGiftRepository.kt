package com.linkid.sdk.all_gift.repository

import android.util.Log
import com.linkid.sdk.all_gift.service.AllGiftService
import com.linkid.sdk.models.category.Category
import com.linkid.sdk.models.category.GiftCategoryResponseModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AllGiftRepository(private val service: AllGiftService) {
    suspend fun getGiftCategories(): Flow<Result<List<Category>>> =
        service.getGiftCategories().map { result ->
            if (result.isSuccess) {
                val giftCategoryResponseModel: GiftCategoryResponseModel? = result.getOrNull()
                if (giftCategoryResponseModel?.result?.items != null) {
                    Result.success(giftCategoryResponseModel.result.items)
                } else {
                    Result.failure(result.exceptionOrNull()!!)
                }
            } else {
                Log.d(
                    "AllGiftRepository",
                    "getGiftCategories: ${result.exceptionOrNull()?.toString()}"
                )
                Result.failure(result.exceptionOrNull()!!)
            }
        }
}