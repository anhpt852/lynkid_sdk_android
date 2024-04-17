package vn.linkid.sdk.category.repository

import android.util.Log
import vn.linkid.sdk.category.service.CategoryService
import vn.linkid.sdk.models.category.Category
import vn.linkid.sdk.models.category.GiftCategoryResponseModel
import vn.linkid.sdk.models.category.GiftsByCategoryResponseModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CategoryRepository(private val service: CategoryService) {
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
                    "CategoryRepository",
                    "getGiftCategories: ${result.exceptionOrNull()?.toString()}"
                )
                Result.failure(result.exceptionOrNull()!!)
            }
        }
    suspend fun getGiftsByCategoryCode(
        categoryCode: String,
        index: Int
    ): Flow<GiftsByCategoryResponseModel?> =
        service.getGiftsByCategoryCode(categoryCode, index).map { result ->
            if (result.isSuccess) {
                result.getOrNull()
            } else {
                null
            }
        }
}