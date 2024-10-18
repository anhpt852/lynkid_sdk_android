package vn.linkid.sdk.transaction.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import vn.linkid.sdk.models.transaction.GetTransactionItem
import vn.linkid.sdk.transaction.repository.TransactionRepository

class TransactionListViewModel(private val repository: TransactionRepository) : ViewModel() {


    fun getMerchant() = liveData { emitSource(repository.getMerchant().asLiveData()) }


    private val _tab = MutableLiveData<Int>(0)

    fun setTab(tab: Int) {
        _tab.value = tab
    }


    val loader = MutableLiveData(true)
    val isEmpty = MutableLiveData(true)
    val uiState = MediatorLiveData<Pair<Boolean, Boolean>>().apply {
        addSource(loader) { loader ->
            value = Pair(loader, isEmpty.value ?: false)
        }
        addSource(isEmpty) { isEmpty ->
            value = Pair(loader.value ?: false, isEmpty)
        }
    }

    val transactions: LiveData<PagingData<GetTransactionItem>> =
        liveData {
            loader.postValue(true)
            repository.getTransactionsStream(_tab.value ?: 0)
                .cachedIn(viewModelScope)
                .collectLatest { pagingData ->
                    emit(pagingData)
                    delay(1200)
                    loader.postValue(false)
                }
        }


}