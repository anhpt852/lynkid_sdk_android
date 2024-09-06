package vn.linkid.sdk.imedia.repository

import android.util.Log
import kotlinx.coroutines.flow.map
import vn.linkid.sdk.imedia.service.iMediaService

class iMediaRepository(private val service: iMediaService) {

    suspend fun getGiftsByGroupType(groupType: Int, sorting: String) = service.getGiftsByGroupType(groupType, sorting).map { result ->
        if (result.isSuccess) {
            val giftsByGroupType = result.getOrNull()
            if (giftsByGroupType != null && giftsByGroupType.success == true) {
                Result.success(giftsByGroupType.result!!)
            } else {
                Result.failure(result.exceptionOrNull()!!)
            }
        } else {
            Log.d("iMediaRepository", "getGiftsByGroupType: ${result.exceptionOrNull()?.toString()}")
            Result.failure(result.exceptionOrNull()!!)
        }
    }
}