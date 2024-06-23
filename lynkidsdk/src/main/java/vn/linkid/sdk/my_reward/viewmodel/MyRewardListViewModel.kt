package vn.linkid.sdk.my_reward.viewmodel

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
import vn.linkid.sdk.models.my_reward.GiftInfoItem
import vn.linkid.sdk.my_reward.repository.MyRewardListRepository

class MyRewardListViewModel(private val repository: MyRewardListRepository, private val tab: Int) : ViewModel() {


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
    val myRewards: LiveData<PagingData<GiftInfoItem>> =
        liveData {
            loader.postValue(true)
            repository.getMyRewardStream(tab)
                .cachedIn(viewModelScope)
                .collectLatest { pagingData ->
                    emit(pagingData)
                    loader.postValue(false)
                }
        }

}