package vn.linkid.sdk.gift_detail.repository

import kotlinx.coroutines.flow.map
import vn.linkid.sdk.gift_detail.service.GiftDetailService

class GiftDetailRepository(private val service: GiftDetailService) {

    suspend fun getGiftDetail(id: Int) = service.getGiftDetail(id).map { result ->
        if (result.isSuccess) {
            val giftDetailResponseModel = result.getOrNull()
            if (giftDetailResponseModel != null && giftDetailResponseModel.isSuccess == true) {
                Result.success(giftDetailResponseModel.data!!)
            } else {
                Result.failure(result.exceptionOrNull()!!)
            }
        } else {
            Result.failure(result.exceptionOrNull()!!)
        }
    }
}