package vn.linkid.sdk.address.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import vn.linkid.sdk.address.repository.AddressPickerRepository

@Suppress("UNCHECKED_CAST")
class AddressPickerViewModelFactory(private val repository: AddressPickerRepository): ViewModelProvider.Factory{


    override fun <T : ViewModel> create(modelClass: Class<T>): T = AddressPickerViewModel(repository) as T
}