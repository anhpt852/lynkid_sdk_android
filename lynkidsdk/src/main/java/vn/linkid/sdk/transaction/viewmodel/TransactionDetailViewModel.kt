package vn.linkid.sdk.transaction.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import vn.linkid.sdk.transaction.repository.TransactionRepository

class TransactionDetailViewModel(private val repository: TransactionRepository) : ViewModel() {

    fun getTransactionDetail(transactionCode: String, isTokenTransId: Boolean) = liveData {
        if (isTokenTransId) {
            emitSource(repository.getTransactionDetail(transactionCode).asLiveData())
        } else {
            val transactionResult = repository.getSingleTransactionByOrderCode(transactionCode)
                .map { it.getOrNull() }
                .first()
            transactionResult?.let { transaction ->
                if ((transaction.items ?: emptyList()).isNotEmpty()) {
                    emitSource(
                        repository.getTransactionDetail(
                            transaction.items?.get(0)?.tokenTransID ?: ""
                        ).asLiveData()
                    )
                }
            }
        }
    }


}