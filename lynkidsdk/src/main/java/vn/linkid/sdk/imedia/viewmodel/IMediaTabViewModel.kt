package vn.linkid.sdk.imedia.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import vn.linkid.sdk.imedia.repository.IMediaRepository

class IMediaTabViewModel(private val repository: IMediaRepository) : ViewModel() {

    fun getBrandByVendor() =
        liveData { emitSource(repository.getBrandByVendor().asLiveData()) }

    fun getDiscountedIMedia(groupType: Int) =
        liveData { emitSource(repository.getGiftsByGroupType(groupType).asLiveData()) }

    fun getAllIMedia(brandId: Int) =
        liveData { emitSource(repository.getAllIMedia(brandId).asLiveData()) }

}