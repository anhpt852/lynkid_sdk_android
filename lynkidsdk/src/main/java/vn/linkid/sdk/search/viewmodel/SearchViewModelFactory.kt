package vn.linkid.sdk.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import vn.linkid.sdk.search.repository.SearchRepository

@Suppress("UNCHECKED_CAST")
class SearchViewModelFactory(private val repository: SearchRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = SearchViewModel(repository) as T
}