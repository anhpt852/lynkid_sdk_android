package vn.linkid.sdk.imedia.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import vn.linkid.sdk.imedia.repository.IMediaRepository


@Suppress("UNCHECKED_CAST")
class IMediaHistoryTabViewModelFactory(
    private val repository: IMediaRepository,
    private val tab: Int
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        IMediaHistoryTabViewModel(repository, tab) as T
}