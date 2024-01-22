package com.linkid.sdk.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.linkid.sdk.home.models.HomeNewsAndBannerModel
import com.linkid.sdk.home.repository.HomeRepository
import kotlinx.coroutines.flow.onEach

class HomeViewModel(private val repository: HomeRepository) : ViewModel() {
    val bannersAndNewsLoader = MutableLiveData<Boolean>()

    val bannersAndNews = liveData<Result<HomeNewsAndBannerModel>> {
        bannersAndNewsLoader.postValue(true)
        emitSource(repository.getBannerAndNews()
            .onEach {
                bannersAndNewsLoader.postValue(false)
            }
            .asLiveData())
    }
}

