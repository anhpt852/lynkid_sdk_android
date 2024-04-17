package vn.linkid.sdk.category.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import vn.linkid.sdk.category.repository.CategoryRepository
import vn.linkid.sdk.models.category.Gift
import vn.linkid.sdk.models.category.GiftsByCategoryResponseModel
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
    private val totalGiftCount = MutableLiveData<Int>(0)
    val giftsByCategory = MutableLiveData<List<Gift>>(emptyList())
    val categoryCode = MutableLiveData<String>("")
    fun getGiftsByCategory(index: Int) =
        liveData {
            loader.postValue(true)
            emitSource(
                repository.getGiftsByCategoryCode(categoryCode.value ?: "", index)
                    .onEach {
                        loader.postValue(false)
                        totalGiftCount.postValue(it?.data?.totalCount ?: 0)
                        val currentGifts = giftsByCategory.value ?: emptyList()
                        val newGifts = it?.data?.items ?: emptyList()
                        giftsByCategory.postValue(currentGifts + newGifts)
                        giftsByCategory.postValue(if (index == 0) newGifts else currentGifts + newGifts)
                    }.asLiveData()
            )

        }

}