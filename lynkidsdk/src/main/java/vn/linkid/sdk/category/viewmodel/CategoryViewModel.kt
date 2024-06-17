package vn.linkid.sdk.category.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import vn.linkid.sdk.category.repository.CategoryRepository
import vn.linkid.sdk.models.category.Gift
import vn.linkid.sdk.models.category.GiftsByCategoryResponseModel
import kotlinx.coroutines.flow.onEach
import vn.linkid.sdk.models.category.GiftFilterModel

class CategoryViewModel(private val repository: CategoryRepository) : ViewModel() {

    val categoriesLoader = MutableLiveData(true)
    val categoryCode = MutableLiveData<String>("")
    val categories = liveData {
        categoriesLoader.postValue(true)
        emitSource(repository.getGiftCategories()
            .onEach {
                categoriesLoader.postValue(false)
            }
            .asLiveData())
    }
    fun initCateCode(code: String) {
        if (categoryCode.value.isNullOrEmpty()) {
            categoryCode.postValue(code)
        }
    }
    fun setCateCode(code: String) {
        categoryCode.postValue(code)
    }

    val loader = MutableLiveData(false)
    val isEmpty = MutableLiveData(false)
    val uiState = MediatorLiveData<Pair<Boolean, Boolean>>().apply {
        addSource(loader) { loader ->
            value = Pair(loader, isEmpty.value ?: false)
        }
        addSource(isEmpty) { isEmpty ->
            value = Pair(loader.value ?: false, isEmpty)
        }
    }
    val giftsByCategory: LiveData<PagingData<Gift>> = categoryCode.switchMap { categoryCode ->
        liveData {
            loader.postValue(true)
            repository.getGiftsStream(categoryCode)
                .cachedIn(viewModelScope)
                .collectLatest { pagingData ->
                    emit(pagingData)
                    loader.postValue(false)
                }
        }
    }

//    private var currentPage = 0
//    private var isLastPage = false
//    fun getGiftsByCategory(loadMore: Boolean = false) = liveData<Any?> {
//        // Only fetch more data if it's not the last page or if it's a refresh (loadMore is false).
//        if (isLastPage && loadMore) {
//            emit(giftsByCategory.value) // Emit current list to stop loading more.
//            return@liveData
//        }
//
//        // If loading more data, increase page index, otherwise reset for refresh.
//        val index = if (loadMore) ++currentPage else 0
//
//        loader.postValue(true)
//        val categoryCodeValue = if (categoryCode.value == "all") "" else categoryCode.value ?: ""
//
//        emitSource(
//            repository.getGiftsByCategoryCode(categoryCodeValue, index * 10) // Calculate offset based on page index.
//                .onEach { response ->
//                    loader.postValue(false)
//                    response?.data?.let { data ->
//                        if (!loadMore) {
//                            // Reset the list on refresh
//                            giftsByCategory.postValue(data.items ?: emptyList())
//                            currentPage = 0 // Reset the page count on refresh
//                        } else {
//                            // Append new items to the existing list
//                            val updatedList = giftsByCategory.value.orEmpty() + (data.items.orEmpty())
//                            giftsByCategory.postValue(updatedList)
//                        }
//                        // Update pagination control variables
//                        isLastPage = data.items.isNullOrEmpty()
//                        totalGiftCount.postValue(data.totalCount ?: 0)
//                    }
//                }.asLiveData()
//        )
//    }


    val isShowFilter = MutableLiveData(true)
    fun setShowFilter(isScrollingUp: Boolean) {
        isShowFilter.postValue(isScrollingUp)
    }

    val giftFilter = MutableLiveData<GiftFilterModel>(GiftFilterModel())
    fun setGiftFilter(giftFilter: GiftFilterModel) {
        this.giftFilter.postValue(giftFilter)
    }

}