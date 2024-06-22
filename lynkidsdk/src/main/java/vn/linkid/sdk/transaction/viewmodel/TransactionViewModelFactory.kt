package vn.linkid.sdk.transaction.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import vn.linkid.sdk.transaction.repository.TransactionRepository

@Suppress("UNCHECKED_CAST")
class TransactionViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = TransactionViewModel() as T

}