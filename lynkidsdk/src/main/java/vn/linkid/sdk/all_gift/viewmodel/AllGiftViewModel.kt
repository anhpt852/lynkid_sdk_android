package vn.linkid.sdk.all_gift.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import vn.linkid.sdk.all_gift.repository.AllGiftRepository
import kotlinx.coroutines.flow.onEach

class AllGiftViewModel(private val repository: AllGiftRepository) : ViewModel() {


    val categoriesLoader = MutableLiveData(true)
    val categories = liveData {
        categoriesLoader.postValue(true)
        emitSource(repository.getGiftCategories()
            .onEach {
                categoriesLoader.postValue(false)
            }
            .asLiveData())
    }


    val giftGroupLoader = MutableLiveData(true)
    val giftGroups = liveData {
        giftGroupLoader.postValue(true)
        emitSource(repository.getAllGiftGroups()
            .onEach {
                giftGroupLoader.postValue(false)
            }
            .asLiveData())
    }

}