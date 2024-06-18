package vn.linkid.sdk.search.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.time.debounce
import vn.linkid.sdk.models.gift.Gift
import vn.linkid.sdk.search.repository.SearchRepository
import kotlin.time.Duration.Companion.milliseconds

class SearchViewModel(private val repository: SearchRepository) : ViewModel() {

    val loader = MutableLiveData(false)
    val isEmpty = MutableLiveData(false)
    val uiState = MediatorLiveData<Pair<Boolean, Boolean>>().apply {
        addSource(loader) { loader ->
            value = Pair(loader, isEmpty.value ?: false)
        }
        addSource(isEmpty) { isEmpty ->
            value = Pair(loader.value ?: false, isEmpty)
        }
    }

    private val searchingKeyword = MutableLiveData<String>("")
    private val searchKeywordDebounced = searchingKeyword.asFlow()
        .debounce(2000.milliseconds)
        .distinctUntilChanged()
        .asLiveData()

    fun setKeyword(keyword: String) {
        searchingKeyword.value = keyword
    }

    val giftList: LiveData<PagingData<Gift>> = searchKeywordDebounced.switchMap { keyword ->
        liveData {
            loader.postValue(true)
            if (keyword.isEmpty()) {
                emit(PagingData.empty<Gift>())
                loader.postValue(false)
            } else {
                repository.getGiftsStream(keyword)
                    .cachedIn(viewModelScope)
                    .collectLatest { pagingData ->
                        emit(pagingData)
                        loader.postValue(false)
                    }
            }
        }
    }


}