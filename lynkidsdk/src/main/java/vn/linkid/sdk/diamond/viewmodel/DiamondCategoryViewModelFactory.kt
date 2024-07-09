package vn.linkid.sdk.diamond.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import vn.linkid.sdk.diamond.repository.DiamondRepository

@Suppress("UNCHECKED_CAST")
class DiamondCategoryViewModelFactory(private val repository: DiamondRepository, private val diamondCateCode: String): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T = DiamondCategoryViewModel(repository, diamondCateCode) as T

}