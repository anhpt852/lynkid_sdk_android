package com.linkid.sdk.category.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.linkid.sdk.category.repository.CategoryRepository
import com.linkid.sdk.models.category.GiftsByCategoryResponseModel
import kotlinx.coroutines.flow.onEach

class CategoryViewModel(private val repository: CategoryRepository) : ViewModel() {

    val categoriesLoader = MutableLiveData(true)
    val categories = liveData {
        categoriesLoader.postValue(true)
        emitSource(repository.getGiftCategories()
            .onEach {
                categoriesLoader.postValue(false)
            }
            .asLiveData())
    }

    val loader = MutableLiveData(false)
    fun getGiftsByCategory(categoryCode: String, index: Int) =
        liveData {
            loader.postValue(true)
            emitSource(
                repository.getGiftsByCategoryCode(categoryCode, index)
                    .onEach {
                        loader.postValue(false)
                    }.asLiveData()
            )

        }

}