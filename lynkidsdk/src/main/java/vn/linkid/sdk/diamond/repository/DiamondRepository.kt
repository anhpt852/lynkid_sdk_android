package vn.linkid.sdk.diamond.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import vn.linkid.sdk.diamond.paging.DiamondCategoryPagingSource
import vn.linkid.sdk.diamond.service.DiamondService
import vn.linkid.sdk.models.category.Category
import vn.linkid.sdk.models.category.Gift
import vn.linkid.sdk.models.category.GiftFilterModel
import vn.linkid.sdk.models.diamond.DiamondCategory
import vn.linkid.sdk.models.diamond.GetDiamondCategoryResponseModel

class DiamondRepository(private val service: DiamondService) {

    suspend fun getDiamondCategories(diamondCateCode: String): Flow<Result<List<DiamondCategory>>> =
        service.getDiamondCategories(diamondCateCode).map { result ->
            if (result.isSuccess) {
                val getDiamondCategoryResponseModel: GetDiamondCategoryResponseModel? =
                    result.getOrNull()
                if (getDiamondCategoryResponseModel?.result?.items != null) {
                    Result.success(
                        listOf(
                            DiamondCategory(
                                giftCategory = Category(
                                    code = "all",
                                    name = "Tất cả",
                                    description = "",
                                    status = "",
                                    categoryTypeCode = null,
                                    fullLink = null,
                                    imageLink = null,
                                    level = null,
                                    parentCode = null,
                                    parentId = null
                                ),
                                giftInfors = emptyList(),
                                totalCountGiftInfors = 0
                            )
                        ).plus(getDiamondCategoryResponseModel.result.items)
                    )
                } else {
                    Result.failure(result.exceptionOrNull()!!)
                }
            } else {
                Log.d(
                    "getDiamondCategories",
                    "getDiamondCategories: ${result.exceptionOrNull()?.toString()}"
                )
                Result.failure(result.exceptionOrNull()!!)
            }
        }

    fun getGiftsStream(categoryCode: String, parentCode: String?, filter: GiftFilterModel): Flow<PagingData<Gift>> =
        Pager(
            PagingConfig(pageSize = 10, enablePlaceholders = false)
        ) {
            DiamondCategoryPagingSource(service, categoryCode, parentCode, filter)
        }.flow
}