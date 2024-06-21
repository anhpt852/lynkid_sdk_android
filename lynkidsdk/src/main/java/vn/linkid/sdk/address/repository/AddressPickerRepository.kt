package vn.linkid.sdk.address.repository

import android.util.Log
import kotlinx.coroutines.flow.map
import vn.linkid.sdk.address.service.AddressPickerService

class AddressPickerRepository(private val service: AddressPickerService) {

//    suspend fun getAddress(id: Int) = service.getAddress(id).map { result ->
//        if (result.isSuccess) {
//            val giftDetailResponseModel = result.getOrNull()
//            if (giftDetailResponseModel != null && giftDetailResponseModel.isSuccess == true) {
//                Result.success(giftDetailResponseModel.data!!)
//            } else {
//                Result.failure(result.exceptionOrNull()!!)
//            }
//        } else {
//            Log.d("GiftDetailRepository", "getGiftDetail: ${result.exceptionOrNull()?.toString()}")
//            Result.failure(result.exceptionOrNull()!!)
//        }
//    }

}