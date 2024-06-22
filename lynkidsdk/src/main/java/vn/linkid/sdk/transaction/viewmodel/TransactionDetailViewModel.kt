package vn.linkid.sdk.transaction.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import vn.linkid.sdk.transaction.repository.TransactionRepository

class TransactionDetailViewModel(private val repository: TransactionRepository) : ViewModel() {

    fun getTransactionDetail(transactionCode: String) = liveData { emitSource(repository.getTransactionDetail(transactionCode).asLiveData()) }

}