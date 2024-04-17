package vn.linkid.sdk.auth.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import vn.linkid.sdk.auth.repository.RegisterRepository
import vn.linkid.sdk.models.auth.ConnectedMember
import kotlinx.coroutines.flow.onEach

class RegisterViewModel(private val repository: RegisterRepository) : ViewModel() {
    val loader = MutableLiveData(false)
    fun createMember() = liveData {
        loader.postValue(true)
        emitSource(repository.createMember()
            .onEach {
                loader.postValue(false)
            }
            .asLiveData())
    }
}