package vn.linkid.sdk.transaction.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.flow.first
import vn.linkid.sdk.models.transaction.GetTransactionDetailResponseModel
import vn.linkid.sdk.transaction.service.TransactionService

class TransactionPagingSource(private val service: TransactionService, private val tab: Int) :
    PagingSource<Int, GetTransactionDetailResponseModel>() {

    override fun getRefreshKey(state: PagingState<Int, GetTransactionDetailResponseModel>): Int? {
        return state.anchorPosition?.let { position ->
            val anchorPage = state.closestPageToPosition(position)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GetTransactionDetailResponseModel> {
        val pageIndex = params.key ?: 0

        return try {
            val result = service.getTransactions(pageIndex, tab).first()
            result.fold(
                onSuccess = { response ->
                    val totalCount = response.totalCount ?: 0
                    LoadResult.Page(
                        data = response.items ?: emptyList(),
                        prevKey = if (pageIndex == 0) null else pageIndex - 1,
                        nextKey = if (response.items.isNullOrEmpty() or ((totalCount > 0) and (totalCount <= ((pageIndex + 1) * 10)))) null else pageIndex + 1
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