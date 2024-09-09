package vn.linkid.sdk.imedia.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import vn.linkid.sdk.imedia.repository.IMediaRepository


@Suppress("UNCHECKED_CAST")
class IMediaTabViewModelFactory(private val repository: IMediaRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        IMediaTabViewModel(repository) as T
}