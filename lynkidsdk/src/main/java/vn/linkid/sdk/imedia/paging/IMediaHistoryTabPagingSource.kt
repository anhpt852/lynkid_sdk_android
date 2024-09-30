package vn.linkid.sdk.imedia.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.flow.first
import vn.linkid.sdk.imedia.service.IMediaService
import vn.linkid.sdk.models.category.Gift
import vn.linkid.sdk.models.imedia.GetIMediaHistory

class IMediaHistoryTabPagingSource(private val service: IMediaService, private val tab: Int) :
    PagingSource<Int, GetIMediaHistory>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GetIMediaHistory> {
        val pageIndex = params.key ?: 0

        return try {
            val result =
                service.getIMediaHistory(pageIndex, tab).first()
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

    override fun getRefreshKey(state: PagingState<Int, GetIMediaHistory>): Int? {
        return state.anchorPosition?.let { position ->
            val anchorPage = state.closestPageToPosition(position)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}