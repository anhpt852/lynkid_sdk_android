package vn.linkid.sdk.all_gift.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import vn.linkid.sdk.all_gift.repository.GiftGroupRepository

@Suppress("UNCHECKED_CAST")
class GiftGroupViewModelFactory(private val repository: GiftGroupRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        GiftGroupViewModel(repository) as T

}