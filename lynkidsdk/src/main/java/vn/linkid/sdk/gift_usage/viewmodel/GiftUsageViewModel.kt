package vn.linkid.sdk.gift_usage.viewmodel

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
import vn.linkid.sdk.all_gift.repository.GiftGroupRepository
import vn.linkid.sdk.gift_usage.repository.GiftUsageRepository
import vn.linkid.sdk.models.category.Gift
import vn.linkid.sdk.models.gift_usage.GiftUsageAddress

class GiftUsageViewModel(private val repository: GiftUsageRepository) : ViewModel() {

    val giftCode = MutableLiveData<String>("")
    fun setGiftCode(code: String) {
        giftCode.postValue(code)
    }

    val searchName = MutableLiveData<String>("")
    fun setName(name: String) {
        searchName.postValue(name)
    }

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


    private val searchTrigger = MediatorLiveData<Pair<String, String>>().apply {
        // Add giftCode as source
        addSource(giftCode) { code ->
            value = Pair(code ?: "", searchName.value ?: "")
        }
        // Add searchName as source with debounce
        addSource(searchName.asFlow()
            .debounce(500)
            .distinctUntilChanged()
            .asLiveData(viewModelScope.coroutineContext)
        ) { name ->
            value = Pair(giftCode.value ?: "", name)
        }
    }

    val giftUsage: LiveData<PagingData<GiftUsageAddress>> = searchTrigger.switchMap { (giftCode, name) ->
        if(giftCode.isEmpty()) {
            return@switchMap liveData {
                emit(PagingData.empty())
            }
        }
        liveData {
            loader.postValue(true)
            repository.getGiftUsageStream(giftCode, searchName.value)
                .cachedIn(viewModelScope)
                .collectLatest { pagingData ->
                    emit(pagingData)
                    loader.postValue(false)
                }
        }
    }

}