package vn.linkid.sdk.all_gift.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onEach
import vn.linkid.sdk.all_gift.repository.GiftGroupRepository
import vn.linkid.sdk.models.category.Gift
import vn.linkid.sdk.models.category.GiftFilterModel

class GiftGroupViewModel(private val repository: GiftGroupRepository) : ViewModel() {

    val groupCode = MutableLiveData<String>("")
    fun setGroupCode(code: String) {
        groupCode.postValue(code)
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
    val giftsByGroupCode: LiveData<PagingData<Gift>> = groupCode.switchMap { groupCode ->
        if(groupCode.isNullOrEmpty()) {
            return@switchMap liveData {
                emit(PagingData.empty())
            }
        }
        liveData {
            loader.postValue(true)
            repository.getGiftsStream(groupCode)
                .cachedIn(viewModelScope)
                .collectLatest { pagingData ->
                    emit(pagingData)
                    loader.postValue(false)
                }
        }
    }

}