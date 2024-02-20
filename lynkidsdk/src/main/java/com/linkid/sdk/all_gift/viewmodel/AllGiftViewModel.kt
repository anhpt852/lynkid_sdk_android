package com.linkid.sdk.all_gift.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.linkid.sdk.all_gift.repository.AllGiftRepository
import com.linkid.sdk.models.category.Category
import kotlinx.coroutines.flow.onEach

class AllGiftViewModel(private val repository: AllGiftRepository): ViewModel() {


    val categoriesLoader = MutableLiveData<Boolean>(true)
    val categories = liveData<Result<List<Category>>> {
        categoriesLoader.postValue(true)
        emitSource(repository.getGiftCategories()
            .onEach {
                categoriesLoader.postValue(false)
            }
            .asLiveData())
    }

}