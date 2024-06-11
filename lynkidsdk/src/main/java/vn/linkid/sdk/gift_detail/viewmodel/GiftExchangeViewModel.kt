package vn.linkid.sdk.gift_detail.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.flow.onEach
import vn.linkid.sdk.gift_detail.repository.GiftDetailRepository
import vn.linkid.sdk.models.point.Point

class GiftExchangeViewModel(private val repository: GiftDetailRepository) : ViewModel() {

    val isLoading = MutableLiveData<Boolean>(true)
    val quantity = MutableLiveData<Int>(1)
    fun getGiftDetail(id: Int) = liveData {
        emitSource(repository.getGiftDetail(id).asLiveData())
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

    fun increaseQuantity() {
        quantity.value = quantity.value?.plus(1)
    }

    fun decreaseQuantity() {
        quantity.value = quantity.value?.minus(1)
    }

    fun createTransaction(giftCode: String, totalAmount: Double) = liveData {
        emitSource(repository.createTransaction(giftCode, quantity.value ?: 1, totalAmount).asLiveData())
    }
}