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
            repository.getGiftsStream(categoryCode, giftFilter.value ?: GiftFilterModel())
                .cachedIn(viewModelScope)
                .collectLatest { pagingData ->
                    emit(pagingData)
                    loader.postValue(false)
                }
        }
    }


    val isShowFilter = MutableLiveData(true)
    fun setShowFilter(isScrollingUp: Boolean) {
        isShowFilter.postValue(isScrollingUp)
    }

    val giftFilter = MutableLiveData<GiftFilterModel>(GiftFilterModel())
    fun setGiftFilter(giftFilter: GiftFilterModel) {
        this.giftFilter.postValue(giftFilter)
    }

}