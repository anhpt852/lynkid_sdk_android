package vn.linkid.sdk.category.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import vn.linkid.sdk.category.service.CategoryService
import vn.linkid.sdk.models.category.Category
import vn.linkid.sdk.models.category.GiftCategoryResponseModel
import vn.linkid.sdk.models.category.GiftsByCategoryResponseModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import vn.linkid.sdk.category.paging.CategoryPagingSource
import vn.linkid.sdk.models.category.Gift
import vn.linkid.sdk.models.category.HomeCategoryResponseModel

class CategoryRepository(private val service: CategoryService) {
    suspend fun getGiftCategories(): Flow<Result<List<Category>>> =
        service.getGiftCategories().map { result ->
            if (result.isSuccess) {
                val homeCategoryResponseModel: HomeCategoryResponseModel? = result.getOrNull()
                if (homeCategoryResponseModel?.data?.row2 != null) {
                    Result.success(
                        listOf(
                            Category(
                                "all",
                                "Tất cả",
                                "",
                                ""
                            )
                        ).plus(homeCategoryResponseModel.data.row2)
                    )
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

    fun getGiftsStream(categoryCode: String): Flow<PagingData<Gift>> = Pager(
        PagingConfig(pageSize = 50, enablePlaceholders = false)
    ) {
        CategoryPagingSource(service, categoryCode)
    }.flow
}