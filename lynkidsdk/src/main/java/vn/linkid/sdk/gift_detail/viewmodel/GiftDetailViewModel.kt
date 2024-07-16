package vn.linkid.sdk.gift_detail.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import vn.linkid.sdk.gift_detail.repository.GiftDetailRepository
import vn.linkid.sdk.models.flash_sale.GetAllFlashSaleProgramResponseModel

class GiftDetailViewModel(private val repository: GiftDetailRepository) : ViewModel() {

    val isLoading = MutableLiveData<Boolean>(true)
    fun getGiftDetail(id: Int) = liveData {
        emitSource(repository.getGiftDetail(id).asLiveData())
    }

    val flashSaleLoader = MutableLiveData<Boolean>(true)
    val flashSale = liveData<Result<GetAllFlashSaleProgramResponseModel>> {
        flashSaleLoader.postValue(true)
        emitSource(repository.getAllFlashSaleProgram()
            .onEach {
                flashSaleLoader.postValue(false)
            }
            .asLiveData())
    }

    val countdownFlashSaleTime = MutableLiveData<Long>(0)
    fun startCountdownFlashSaleTime() {
        viewModelScope.launch(Dispatchers.Main) {
            val initialTime = countdownFlashSaleTime.value ?: 0
            for (time in initialTime downTo 0) {
                countdownFlashSaleTime.value = time
                delay(1000L)
            }
        }
    }

    val isUserDiamond = MutableLiveData<Boolean>(false)
    fun checkUserDiamond() = liveData {
        emitSource(repository.isUserDiamond().onEach {
            isUserDiamond.postValue(it.getOrNull() ?: false)
        }.asLiveData())
    }

}