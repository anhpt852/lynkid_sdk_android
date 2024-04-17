package vn.linkid.sdk.splash.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import vn.linkid.sdk.models.auth.AuthToken
import vn.linkid.sdk.models.auth.AuthType
import vn.linkid.sdk.models.auth.ConnectedMember
import vn.linkid.sdk.splash.repository.SplashRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SplashViewModel(private val repository: SplashRepository) :ViewModel() {

    val loader = MutableLiveData<Boolean>(true)
    fun generateToken() = liveData<ConnectedMember?> {
        emitSource(repository.generateToken()
            .onEach {
                loader.postValue(false)
            }
            .asLiveData())
    }
}