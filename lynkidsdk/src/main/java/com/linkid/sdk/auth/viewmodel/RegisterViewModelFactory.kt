package com.linkid.sdk.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.linkid.sdk.auth.repository.RegisterRepository

@Suppress("UNCHECKED_CAST")
class RegisterViewModelFactory(private val repository: RegisterRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = RegisterViewModel(repository) as T
}