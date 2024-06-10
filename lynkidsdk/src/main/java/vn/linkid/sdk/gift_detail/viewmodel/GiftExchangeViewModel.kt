package vn.linkid.sdk.gift_detail.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import vn.linkid.sdk.gift_detail.repository.GiftDetailRepository

class GiftExchangeViewModel(private val repository: GiftDetailRepository) : ViewModel() {

    val isLoading = MutableLiveData<Boolean>(true)
    val quantity = MutableLiveData<Int>(1)
    fun getGiftDetail(id: Int) = liveData {
        emitSource(repository.getGiftDetail(id).asLiveData())
    }

    fun increaseQuantity() {
        quantity.value = quantity.value?.plus(1)
    }

    fun decreaseQuantity() {
        quantity.value = quantity.value?.minus(1)
    }
}