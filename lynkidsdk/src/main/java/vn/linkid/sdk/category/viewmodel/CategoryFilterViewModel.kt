package vn.linkid.sdk.category.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CategoryFilterViewModel : ViewModel() {
    val fromCoin = MutableLiveData<Long>(0)
    fun setFromCoin(value: Long) {
        fromCoin.value = value
    }

    val toCoin = MutableLiveData<Long>(0)
    fun setToCoin(value: Long) {
        toCoin.value = value
    }

    val isSuitablePrice = MutableLiveData<Boolean>(false)
    fun setIsSuitablePrice(value: Boolean) {
        isSuitablePrice.value = value
    }

    val selectedRange = MutableLiveData<Int?>(null)
    fun setSelectedRange(value: Int?) {
        selectedRange.value = value
    }

    val giftType = MutableLiveData<Int?>(null)
    fun setGiftType(value: Int?) {
        giftType.value = value
    }

    val isFirstLocation = MutableLiveData<Boolean>(false)
    fun setIsFirstLocation(value: Boolean) {
        isFirstLocation.value = value
    }

    val isSecondLocation = MutableLiveData<Boolean>(false)
    fun setIsSecondLocation(value: Boolean) {
        isSecondLocation.value = value
    }
}