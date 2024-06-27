package vn.linkid.sdk.address.service

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import vn.linkid.sdk.LynkiD_SDK
import vn.linkid.sdk.cache.MainCache
import vn.linkid.sdk.models.address.AddressResponseModel
import vn.linkid.sdk.models.gift.AllGiftGroupResponseModel
import vn.linkid.sdk.utils.APIEndpoints
import vn.linkid.sdk.utils.Endpoints
import vn.linkid.sdk.utils.generateCacheKey

class AddressPickerService(private val api: APIEndpoints) {

    suspend fun getAddress(
        parentCode: String?,
        level: String?
    ): Flow<Result<AddressResponseModel>> = flow {
        val params: MutableMap<String, Any> = mutableMapOf(
            "MaxResultCount" to 99,
            "VendorType" to "LinkID"
        )
        if (!parentCode.isNullOrEmpty() && level != "City") {
            params["ParentCodeFilter"] = parentCode
        }
        if (!level.isNullOrEmpty()) {
            params["LevelFilter"] = level
        }
        val cacheKey = generateCacheKey(Endpoints.GET_LOCATIONS, params)
        val cachedResponse = MainCache.get<AddressResponseModel>(cacheKey)
        if (cachedResponse != null) {
            emit(Result.success(cachedResponse))
        } else {
            val response = api.getLocations(queries = params)
            MainCache.put(cacheKey, response)
            emit(Result.success(response))
        }
    }.catch {
        Log.e("AddressPickerService", "getAddress: ${it.message}")
        emit(Result.failure(RuntimeException("Something went wrong")))
    }

}