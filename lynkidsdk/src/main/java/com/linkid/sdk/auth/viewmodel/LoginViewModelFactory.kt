package com.linkid.sdk.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.linkid.sdk.auth.repository.LoginRepository

@Suppress("UNCHECKED_CAST")
class LoginViewModelFactory(private val repository: LoginRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = LoginViewModel(repository) as T
}