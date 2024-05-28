package vn.linkid.sdk.my_reward.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import vn.linkid.sdk.my_reward.repository.MyRewardRepository

@Suppress("UNCHECKED_CAST")
class MyRewardViewModelFactory(private val repository: MyRewardRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        MyRewardViewModel(repository) as T
}