package vn.linkid.sdk.transaction.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import vn.linkid.sdk.models.transaction.TransactionItem
import vn.linkid.sdk.transaction.paging.TransactionPagingSource
import vn.linkid.sdk.transaction.service.TransactionService

class TransactionRepository(private val service: TransactionService) {

    fun getTransactionsStream(tab: Int): Flow<PagingData<TransactionItem>> = Pager(
        PagingConfig(pageSize = 10, enablePlaceholders = false)
    ) {
        TransactionPagingSource(service, tab)
    }.flow
}