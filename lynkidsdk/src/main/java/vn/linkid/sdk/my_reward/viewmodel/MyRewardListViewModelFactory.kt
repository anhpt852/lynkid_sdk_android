package vn.linkid.sdk.my_reward.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import vn.linkid.sdk.my_reward.repository.MyRewardListRepository

@Suppress("UNCHECKED_CAST")
class MyRewardListViewModelFactory(private val repository: MyRewardListRepository, private val tab: Int) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        MyRewardListViewModel(repository, tab) as T
}