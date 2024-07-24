package vn.linkid.sdk.all_gift.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.flow.first
import vn.linkid.sdk.all_gift.service.GiftGroupService
import vn.linkid.sdk.category.service.CategoryService
import vn.linkid.sdk.models.category.Gift
import vn.linkid.sdk.models.category.GiftFilterModel

class GiftGroupPagingSource(private val service: GiftGroupService, private val groupCode: String) :
    PagingSource<Int, Gift>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Gift> {
        val pageIndex = params.key ?: 0

        return try {
            val result = service.getGiftsByGroupCode(groupCode, pageIndex).first()
            result.fold(
                onSuccess = { response ->
                    val totalCount = response.data?.totalCount ?: 0
                    LoadResult.Page(
                        data = response.data?.items ?: emptyList(),
                        prevKey = if (pageIndex == 0) null else pageIndex - 1,
                        nextKey = if (response.data?.items.isNullOrEmpty() or ((totalCount > 0) and (totalCount <= ((pageIndex + 1) * 10)))) null else pageIndex + 1
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

    override fun getRefreshKey(state: PagingState<Int, Gift>): Int? {
        return state.anchorPosition?.let { position ->
            val anchorPage = state.closestPageToPosition(position)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}