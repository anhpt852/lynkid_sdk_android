package com.linkid.sdk.splash.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.linkid.sdk.splash.repository.SplashRepository

@Suppress("UNCHECKED_CAST")
class SplashViewModelFactory(private val repository: SplashRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T = SplashViewModel(repository) as T


}