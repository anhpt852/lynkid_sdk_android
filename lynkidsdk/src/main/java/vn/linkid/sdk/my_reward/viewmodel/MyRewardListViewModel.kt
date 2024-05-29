package vn.linkid.sdk.my_reward.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import vn.linkid.sdk.my_reward.repository.MyRewardListRepository

class MyRewardListViewModel(private val repository: MyRewardListRepository) : ViewModel() {

    val isLoading = MutableLiveData<Boolean>(true)
    val page = MutableLiveData<Int>(0)

    fun getMyRewards() = liveData {
        emitSource(repository.getMyRewards(page.value ?: 0).asLiveData())
    }

}