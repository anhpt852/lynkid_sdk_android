package vn.linkid.sdk.gift_usage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import vn.linkid.sdk.gift_usage.repository.GiftUsageRepository

@Suppress("UNCHECKED_CAST")
class GiftUsageViewModelFactory(private val repository: GiftUsageRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        GiftUsageViewModel(repository) as T

}