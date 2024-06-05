package vn.linkid.sdk.gift_detail.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import vn.linkid.sdk.gift_detail.repository.GiftDetailRepository

class GiftDetailViewModel(private val repository: GiftDetailRepository): ViewModel() {

    val isLoading = MutableLiveData<Boolean>(true)
    fun getGiftDetail(id: Int) = liveData {
        emitSource(repository.getGiftDetail(id).asLiveData())
    }

}