package vn.linkid.sdk.address.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.flow.onEach
import vn.linkid.sdk.address.repository.AddressPickerRepository

class AddressPickerViewModel(private val repository: AddressPickerRepository) : ViewModel() {

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

    fun getAddress(parentCode: String?, level: String?) = liveData {
        loader.postValue(true)
        emitSource(repository.getAddress(parentCode, level)
            .onEach {
                loader.postValue(false)
            }
            .asLiveData())
    }

}