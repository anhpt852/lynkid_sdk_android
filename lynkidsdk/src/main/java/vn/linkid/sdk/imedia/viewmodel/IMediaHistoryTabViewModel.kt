package vn.linkid.sdk.imedia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.collectLatest
import vn.linkid.sdk.imedia.repository.IMediaRepository
import vn.linkid.sdk.models.gift.GiftDetail
import vn.linkid.sdk.models.imedia.GetIMediaHistory
import vn.linkid.sdk.models.imedia.GetThirdPartyBrandByVendor

class IMediaHistoryTabViewModel(private val repository: IMediaRepository, private val tab: Int) :
    ViewModel() {

    val loader = MutableLiveData(true)
    val isEmpty = MutableLiveData(false)
    val uiState = MediatorLiveData<Pair<Boolean, Boolean>>().apply {
        addSource(loader) { loader ->
            value = Pair(loader, isEmpty.value ?: false)
        }
        addSource(isEmpty) { isEmpty ->
            value = Pair(loader.value ?: false, isEmpty)
        }
    }
    val iMediaHistory: LiveData<PagingData<GetIMediaHistory>> = liveData {
        loader.postValue(true)
        repository.getIMediaHistoryStream(tab)
            .cachedIn(viewModelScope)
            .collectLatest { pagingData ->
                emit(pagingData)
                loader.postValue(false)
            }
    }

}