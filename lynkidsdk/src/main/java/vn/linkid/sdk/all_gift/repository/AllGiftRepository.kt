package vn.linkid.sdk.all_gift.repository

import android.util.Log
import vn.linkid.sdk.all_gift.service.AllGiftService
import vn.linkid.sdk.models.category.Category
import vn.linkid.sdk.models.category.GiftCategoryResponseModel
import vn.linkid.sdk.models.gift.AllGiftGroupResponseModel
import vn.linkid.sdk.models.gift.HomeGiftGroup
import vn.linkid.sdk.models.gift.HomeGiftGroupResponseModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import vn.linkid.sdk.models.category.HomeCategoryResponseModel

class AllGiftRepository(private val service: AllGiftService) {
    suspend fun getGiftCategories(): Flow<Result<List<Category>>> =
        service.getGiftCategories().map { result ->
            if (result.isSuccess) {
                val homeCategoryResponseModel: HomeCategoryResponseModel? = result.getOrNull()
                if (homeCategoryResponseModel?.data?.row2 != null) {
                    Result.success(homeCategoryResponseModel.data.row2)
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

    suspend fun getAllGiftGroups(): Flow<Result<List<HomeGiftGroup>>> =
        service.getAllGiftGroups().map { result ->
            if (result.isSuccess) {
                val giftGroupResponseModel: AllGiftGroupResponseModel? = result.getOrNull()
                if (giftGroupResponseModel?.data?.items != null) {
                    Result.success(giftGroupResponseModel.data.items)
                } else {
                    Result.failure(result.exceptionOrNull()!!)
                }
            } else {
                Log.d(
                    "AllGiftRepository",
                    "getAllGiftGroups: ${result.exceptionOrNull()?.toString()}"
                )
                Result.failure(result.exceptionOrNull()!!)
            }
        }
}