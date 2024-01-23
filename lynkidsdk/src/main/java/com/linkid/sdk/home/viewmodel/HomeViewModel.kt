package com.linkid.sdk.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.linkid.sdk.home.models.HomeNewsAndBannerModel
import com.linkid.sdk.home.models.Member
import com.linkid.sdk.home.models.MemberResponseModel
import com.linkid.sdk.home.models.Point
import com.linkid.sdk.home.repository.HomeRepository
import kotlinx.coroutines.flow.onEach

class HomeViewModel(private val repository: HomeRepository) : ViewModel() {

    val memberInfoLoader = MutableLiveData<Boolean>(true)
    val memberInfo = liveData<Result<Member>> {
        memberInfoLoader.postValue(true)
        emitSource(repository.getMemberInfo()
            .onEach {
                memberInfoLoader.postValue(false)
            }
            .asLiveData())
    }

    val pointInfoLoader = MutableLiveData<Boolean>(true)
    val pointInfo = liveData<Result<Point>> {
        pointInfoLoader.postValue(true)
        emitSource(repository.getPointInfo()
            .onEach {
                pointInfoLoader.postValue(false)
            }
            .asLiveData())
    }

    val bannersAndNewsLoader = MutableLiveData<Boolean>(true)
    val bannersAndNews = liveData<Result<HomeNewsAndBannerModel>> {
        bannersAndNewsLoader.postValue(true)
        emitSource(repository.getBannerAndNews()
            .onEach {
                bannersAndNewsLoader.postValue(false)
            }
            .asLiveData())
    }
}

