package com.linkid.sdk.splash.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel :ViewModel() {

    val loader = MutableLiveData<Boolean>()

    init {
        CoroutineScope(Dispatchers.Main).launch {
            delay(3000) // delay for 3 seconds
            loader.value = true
        }
    }



}