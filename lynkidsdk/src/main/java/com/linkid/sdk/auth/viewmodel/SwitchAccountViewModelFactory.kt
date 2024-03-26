package com.linkid.sdk.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.linkid.sdk.auth.repository.SwitchAccountRepository

@Suppress("UNCHECKED_CAST")
class SwitchAccountViewModelFactory(private val repository: SwitchAccountRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = SwitchAccountViewModel(repository) as T
}