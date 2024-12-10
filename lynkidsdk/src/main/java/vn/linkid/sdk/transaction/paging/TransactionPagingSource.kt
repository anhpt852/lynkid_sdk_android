package vn.linkid.sdk.transaction.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.flow.first
import vn.linkid.sdk.models.transaction.GetTransactionItem
import vn.linkid.sdk.models.transaction.GroupTransactionItem
import vn.linkid.sdk.transaction.service.TransactionService

class TransactionPagingSource(private val service: TransactionService, private val tab: Int) :
    PagingSource<Int, GroupTransactionItem>() {

    override fun getRefreshKey(state: PagingState<Int, GroupTransactionItem>): Int? {
        return state.anchorPosition?.let { position ->
            val anchorPage = state.closestPageToPosition(position)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GroupTransactionItem> {
        val pageIndex = params.key ?: 0

        return try {
            val result = service.getTransactions(pageIndex, tab).first()
            result.fold(
                onSuccess = { response ->
                    val totalCount = response.totalCount ?: 0
                    LoadResult.Page(
                        data = parseTransactionItem(response.items ?: emptyList()),
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

    private fun parseTransactionItem(transactionItem: List<GetTransactionItem>): List<GroupTransactionItem> {
        val groupTransactionItem = mutableListOf<GroupTransactionItem>()
        transactionItem.forEach { transaction ->
            val date = transaction.time?.substringBefore("T") ?: ""
            val group = groupTransactionItem.find { it.date == date }
            if (group != null) {
                group.transactions.add(transaction)
            } else {
                groupTransactionItem.add(GroupTransactionItem(date, mutableListOf(transaction)))
            }
        }
        return groupTransactionItem
    }
}