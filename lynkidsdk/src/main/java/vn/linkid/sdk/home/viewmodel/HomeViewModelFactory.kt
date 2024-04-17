package vn.linkid.sdk.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import vn.linkid.sdk.home.repository.HomeRepository

@Suppress("UNCHECKED_CAST")
class HomeViewModelFactory(private val repository: HomeRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T = HomeViewModel(repository) as T


}