package vn.linkid.sdk.imedia.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.flow.onEach
import vn.linkid.sdk.gift_detail.repository.GiftDetailRepository
import vn.linkid.sdk.imedia.repository.IMediaRepository
import vn.linkid.sdk.models.gift.GiftDetail
import vn.linkid.sdk.models.imedia.GetThirdPartyBrandByVendor
import vn.linkid.sdk.models.point.Point

class IMediaTabViewModel(private val repository: IMediaRepository, private val giftDetailRepository: GiftDetailRepository) : ViewModel() {

    val selectedBrand = MutableLiveData<GetThirdPartyBrandByVendor?>()
    val brandList = MutableLiveData<List<GetThirdPartyBrandByVendor>>()
    val selectedGift = MutableLiveData<GiftDetail?>()

    fun getBrandByVendor(tab: Int) =
        liveData { emitSource(repository.getBrandByVendor(tab).asLiveData()) }

    fun getDiscountedIMedia(groupType: Int) =
        liveData { emitSource(repository.getGiftsByGroupType(groupType).asLiveData()) }

    fun getAllIMedia(brandId: Int) =
        liveData { emitSource(repository.getAllIMedia(brandId).asLiveData()) }


    val pointInfo = liveData<Result<Point>> {
        emitSource(giftDetailRepository.getPointInfo()
            .asLiveData())
    }

}