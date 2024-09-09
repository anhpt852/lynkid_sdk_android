package vn.linkid.sdk.imedia.repository

import android.util.Log
import kotlinx.coroutines.flow.map
import vn.linkid.sdk.imedia.service.IMediaService

class IMediaRepository(private val service: IMediaService) {

    suspend fun getBrandByVendor() = service.getBrandByVendor().map { result ->
        if (result.isSuccess) {
            val brandsByVendor = result.getOrNull()
            if (brandsByVendor != null && brandsByVendor.success == true) {
                Result.success(brandsByVendor.result!!)
            } else {
                Result.failure(result.exceptionOrNull()!!)
            }
        } else {
            Log.d("iMediaRepository", "getBrandByVendor: ${result.exceptionOrNull()?.toString()}")
            Result.failure(result.exceptionOrNull()!!)
        }
    }

    suspend fun getGiftsByGroupType(groupType: Int) =
        service.getGiftsByGroupType(groupType).map { result ->
            if (result.isSuccess) {
                val giftsByGroupType = result.getOrNull()
                if (giftsByGroupType != null && giftsByGroupType.success == true) {
                    Result.success(giftsByGroupType.result!!)
                } else {
                    Result.failure(result.exceptionOrNull()!!)
                }
            } else {
                Log.d(
                    "iMediaRepository",
                    "getGiftsByGroupType: ${result.exceptionOrNull()?.toString()}"
                )
                Result.failure(result.exceptionOrNull()!!)
            }
        }

    suspend fun getAllIMedia(brandId: Int) =
        service.getAllIMedia(brandId).map { result ->
            if (result.isSuccess) {
                val iMediaGifts = result.getOrNull()
                if (iMediaGifts != null && iMediaGifts.success == true) {
                    Result.success(iMediaGifts.result!!)
                } else {
                    Result.failure(result.exceptionOrNull()!!)
                }
            } else {
                Log.d(
                    "iMediaRepository",
                    "getAllIMedia: ${result.exceptionOrNull()?.toString()}"
                )
                Result.failure(result.exceptionOrNull()!!)
            }
        }
}