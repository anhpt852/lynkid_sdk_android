package vn.linkid.sdk.address.repository

import android.util.Log
import kotlinx.coroutines.flow.map
import vn.linkid.sdk.address.service.AddressPickerService

class AddressPickerRepository(private val service: AddressPickerService) {
    suspend fun getAddress(
        parentCode: String?,
        level: String?
    ) = service.getAddress(parentCode, level).map { result ->
        if (result.isSuccess) {
            val addressResponseModel = result.getOrNull()
            if (addressResponseModel?.result != null && addressResponseModel.success == true) {
                Result.success(addressResponseModel.result)
            } else {
                Result.failure(result.exceptionOrNull()!!)
            }
        } else {
            Log.d("AddressPickerRepository", "getAddress: ${result.exceptionOrNull()?.toString()}")
            Result.failure(result.exceptionOrNull()!!)
        }
    }

}