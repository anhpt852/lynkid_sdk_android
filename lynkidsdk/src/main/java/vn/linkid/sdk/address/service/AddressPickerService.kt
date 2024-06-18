package vn.linkid.sdk.address.service

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import vn.linkid.sdk.LynkiD_SDK
import vn.linkid.sdk.models.address.AddressResponseModel
import vn.linkid.sdk.utils.APIEndpoints

class AddressPickerService(private val api: APIEndpoints) {

    suspend fun getAddress(
        keyword: String,
        index: Int
    ): Flow<Result<AddressResponseModel>> = flow {
        emit(
            Result.success(
                api.getLocations(
                    queries = mutableMapOf(
                        "MemberCode" to LynkiD_SDK.memberCode,
                        "Filter" to keyword,
                        "SkipCount" to index * 10,
                        "MaxItem" to 10
                    )
                )
            )
        )
    }.catch {
        Log.e("AddressPickerService", "getAddress: ${it.message}")
        emit(Result.failure(RuntimeException("Something went wrong")))
    }

}