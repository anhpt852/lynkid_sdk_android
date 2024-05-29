package vn.linkid.sdk.my_reward.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import vn.linkid.sdk.my_reward.repository.MyRewardDetailRepository

@Suppress("UNCHECKED_CAST")
class MyRewardDetailViewModelFactory(private val repository: MyRewardDetailRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        MyRewardDetailViewModel(repository) as T
}