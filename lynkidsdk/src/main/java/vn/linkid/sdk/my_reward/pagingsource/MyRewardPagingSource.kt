package vn.linkid.sdk.my_reward.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.flow.first
import vn.linkid.sdk.models.my_reward.GiftInfoItem
import vn.linkid.sdk.my_reward.service.MyRewardListService

class MyRewardPagingSource(private val service: MyRewardListService, private val tab: Int) :
    PagingSource<Int, GiftInfoItem>() {

    override fun getRefreshKey(state: PagingState<Int, GiftInfoItem>): Int? {
        return state.anchorPosition?.let { position ->
            val anchorPage = state.closestPageToPosition(position)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GiftInfoItem> {
        val pageIndex = params.key ?: 0

        return try {
            val result = service.getMyRewards(pageIndex, if (tab == 0) "R" else "U;E").first()
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
}