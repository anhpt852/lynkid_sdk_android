package vn.linkid.sdk.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import vn.linkid.sdk.auth.repository.AuthRepository

@Suppress("UNCHECKED_CAST")
class AuthViewModelFactory(private val repository: AuthRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = AuthViewModel(repository) as T
}