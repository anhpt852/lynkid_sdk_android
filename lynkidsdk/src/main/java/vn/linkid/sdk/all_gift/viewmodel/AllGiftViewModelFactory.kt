package vn.linkid.sdk.all_gift.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import vn.linkid.sdk.all_gift.repository.AllGiftRepository

@Suppress("UNCHECKED_CAST")
class AllGiftViewModelFactory(private val repository: AllGiftRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T = AllGiftViewModel(repository) as T


}