package vn.linkid.sdk.my_reward.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import vn.linkid.sdk.my_reward.repository.MyRewardDetailRepository

class MyRewardDetailViewModel(private val repository: MyRewardDetailRepository) : ViewModel() {

    fun getMyRewardDetail(transactionCode: String) = liveData { emitSource(repository.getMyRewardDetail(transactionCode).asLiveData()) }

    fun getFullAddress(cityCode: String, districtCode: String, wardCode: String) = liveData { emitSource(repository.getFullAddress(cityCode, districtCode, wardCode).asLiveData()) }

}