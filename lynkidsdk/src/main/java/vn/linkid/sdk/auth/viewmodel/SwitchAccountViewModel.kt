package vn.linkid.sdk.auth.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import vn.linkid.sdk.auth.repository.SwitchAccountRepository
import vn.linkid.sdk.models.auth.ConnectedMember
import kotlinx.coroutines.flow.onEach

class SwitchAccountViewModel(private val repository: SwitchAccountRepository): ViewModel() {
    val currentOption = MutableLiveData<Int>(0)

    val loader = MutableLiveData<Boolean>(false)
    fun switchMember() = liveData<Boolean?> {
        loader.postValue(true)
        emitSource(repository.switchMember()
            .onEach {
                loader.postValue(false)
            }
            .asLiveData())
    }
    fun createMember() = liveData<ConnectedMember?> {
        loader.postValue(true)
        emitSource(repository.createMember()
            .onEach {
                loader.postValue(false)
            }
            .asLiveData())
    }
}