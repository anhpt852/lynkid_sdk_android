package vn.linkid.sdk.transaction.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import vn.linkid.sdk.models.transaction.GetTransactionItem
import vn.linkid.sdk.transaction.paging.TransactionPagingSource
import vn.linkid.sdk.transaction.service.TransactionService

class TransactionRepository(private val service: TransactionService) {

    fun getTransactionsStream(tab: Int): Flow<PagingData<GetTransactionItem>> = Pager(
        PagingConfig(pageSize = 10, enablePlaceholders = false)
    ) {
        TransactionPagingSource(service, tab)
    }.flow

    suspend fun getTransactionDetail(transactionCode: String) =
        service.getTransactionDetail(transactionCode).map { result ->
            if (result.isSuccess) {
                Result.success(result.getOrNull())
            } else {
                Log.d(
                    "TransactionRepository",
                    "getTransactionDetail: ${result.exceptionOrNull()?.toString()}"
                )
                Result.failure(result.exceptionOrNull()!!)
            }
        }

    suspend fun getMerchant() =
        service.getMerchant().map { result ->
            if (result.isSuccess) {
                Result.success(result.getOrNull())
            } else {
                Log.d(
                    "TransactionRepository",
                    "getMerchant: ${result.exceptionOrNull()?.toString()}"
                )
                Result.failure(result.exceptionOrNull()!!)
            }
        }
}