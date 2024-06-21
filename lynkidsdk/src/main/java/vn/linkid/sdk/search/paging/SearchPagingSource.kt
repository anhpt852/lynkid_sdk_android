package vn.linkid.sdk.search.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.flow.first
import vn.linkid.sdk.models.gift.Gift
import vn.linkid.sdk.search.service.SearchService


class SearchPagingSource(private val service: SearchService, private val keyword: String) :
    PagingSource<Int, Gift>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Gift> {
        val pageIndex = params.key ?: 0

        return try {
            val result = service.searchGift(keyword, pageIndex).first()
            result.fold(
                onSuccess = { response ->
                    val totalCount = response.data?.items?.first()?.numberOfGifts ?: 0
                    LoadResult.Page(
                        data = response.data?.items?.first()?.gifts ?: emptyList(),
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