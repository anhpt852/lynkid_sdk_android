package vn.linkid.sdk.imedia.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import vn.linkid.sdk.imedia.repository.IMediaRepository
import vn.linkid.sdk.models.gift.GiftDetail
import vn.linkid.sdk.models.imedia.GetThirdPartyBrandByVendor

class IMediaTabViewModel(private val repository: IMediaRepository) : ViewModel() {

    val selectedBrand = MutableLiveData<GetThirdPartyBrandByVendor?>()
    val brandList = MutableLiveData<List<GetThirdPartyBrandByVendor>>()
    val selectedGift = MutableLiveData<GiftDetail?>()

    fun getBrandByVendor(tab: Int) =
        liveData { emitSource(repository.getBrandByVendor(tab).asLiveData()) }

    fun getDiscountedIMedia(groupType: Int) =
        liveData { emitSource(repository.getGiftsByGroupType(groupType).asLiveData()) }

    fun getAllIMedia(brandId: Int) =
        liveData { emitSource(repository.getAllIMedia(brandId).asLiveData()) }

}