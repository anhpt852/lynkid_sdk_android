package vn.linkid.sdk.gift_detail.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import vn.linkid.sdk.gift_detail.repository.GiftDetailRepository

@Suppress("UNCHECKED_CAST")
class GiftDetailViewModelFactory(private val repository: GiftDetailRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T = GiftDetailViewModel(repository) as T
}