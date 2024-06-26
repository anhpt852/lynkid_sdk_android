package vn.linkid.sdk.gift_detail.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.flow.onEach
import vn.linkid.sdk.gift_detail.repository.GiftDetailRepository
import vn.linkid.sdk.models.point.Point

class GiftOTPViewModel(private val repository: GiftDetailRepository) : ViewModel() {

    val isLoading = MutableLiveData<Boolean>(false)
    val sessionId = MutableLiveData<String>("")
    fun confirmTransaction(otpCode: String) = liveData {
        isLoading.postValue(true)
        emitSource(
            repository.confirmTransaction(sessionId.value!!, otpCode).onEach { isLoading.postValue(false) }
                .asLiveData()
        )
    }
}