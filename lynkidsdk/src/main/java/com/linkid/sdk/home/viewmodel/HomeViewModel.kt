package com.linkid.sdk.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.linkid.sdk.models.category.Category
import com.linkid.sdk.models.banner.HomeNewsAndBannerModel
import com.linkid.sdk.models.member.Member
import com.linkid.sdk.models.point.Point
import com.linkid.sdk.home.repository.HomeRepository
import com.linkid.sdk.models.gift.HomeGiftGroupResponseModel
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

    val categoriesLoader = MutableLiveData<Boolean>(true)
    val categories = liveData<Result<List<Category>>> {
        categoriesLoader.postValue(true)
        emitSource(repository.getHomeCategories()
            .onEach {
                categoriesLoader.postValue(false)
            }
            .asLiveData())
    }

    val homeGiftGroupLoader = MutableLiveData<Boolean>(true)
    val homeGiftGroup = liveData<Result<HomeGiftGroupResponseModel>> {
        homeGiftGroupLoader.postValue(true)
        emitSource(repository.getHomeGiftGroup()
            .onEach {
                homeGiftGroupLoader.postValue(false)
            }
            .asLiveData())
    }
}

