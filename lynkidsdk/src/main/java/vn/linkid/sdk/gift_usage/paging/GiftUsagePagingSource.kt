package vn.linkid.sdk.gift_usage.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.flow.first
import vn.linkid.sdk.all_gift.service.GiftGroupService
import vn.linkid.sdk.category.service.CategoryService
import vn.linkid.sdk.gift_usage.service.GiftUsageService
import vn.linkid.sdk.models.category.Gift
import vn.linkid.sdk.models.category.GiftFilterModel
import vn.linkid.sdk.models.gift_usage.GiftUsageAddress

class GiftUsagePagingSource(
    private val service: GiftUsageService,
    private val giftCode: String,
    private val name: String?,
) :
    PagingSource<Int, GiftUsageAddress>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GiftUsageAddress> {
        val pageIndex = params.key ?: 0

        return try {
            val result = service.getGiftUsage(pageIndex, giftCode, name).first()
            result.fold(
                onSuccess = { response ->
                    val totalCount = response.result?.totalCount ?: 0
                    LoadResult.Page(
                        data = response.result?.items ?: emptyList(),
                        prevKey = if (pageIndex == 0) null else pageIndex - 1,
                        nextKey = if (response.result?.items.isNullOrEmpty() or ((totalCount > 0) and (totalCount <= ((pageIndex + 1) * 10)))) null else pageIndex + 1
                    )
                },
                onFailure = { exception ->
                    LoadResult.Error(exception)
                }
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, GiftUsageAddress>): Int? {
        return state.anchorPosition?.let { position ->
            val anchorPage = state.closestPageToPosition(position)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}