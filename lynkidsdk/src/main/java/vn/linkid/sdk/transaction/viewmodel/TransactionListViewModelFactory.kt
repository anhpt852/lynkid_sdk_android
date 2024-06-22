package vn.linkid.sdk.transaction.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import vn.linkid.sdk.transaction.repository.TransactionRepository

@Suppress("UNCHECKED_CAST")
class TransactionListViewModelFactory(private val repository: TransactionRepository, private val tab: Int) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        TransactionListViewModel(repository, tab) as T
}