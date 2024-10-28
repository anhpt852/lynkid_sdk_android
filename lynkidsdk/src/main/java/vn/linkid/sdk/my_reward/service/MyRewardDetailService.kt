package vn.linkid.sdk.my_reward.service

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import vn.linkid.sdk.utils.APIEndpoints
import vn.linkid.sdk.LynkiD_SDK
import vn.linkid.sdk.cache.MainCache
import vn.linkid.sdk.models.address.AddressResponseModel
import vn.linkid.sdk.models.my_reward.MyRewardListResponseModel
import vn.linkid.sdk.utils.Endpoints
import vn.linkid.sdk.utils.generateCacheKey

class MyRewardDetailService(private val api: APIEndpoints) {
    suspend fun getMyRewardDetail(
        transactionCode: String,
    ): Flow<Result<MyRewardListResponseModel>> = flow {
        val params: MutableMap<String, Any> = mutableMapOf(
            "OwnerCodeFilter" to LynkiD_SDK.memberCode,
            "GiftTransactionCode" to transactionCode,
            "Ver" to "next"
        )
        val cacheKey = generateCacheKey(Endpoints.GET_MY_REWARDS, params)
        val cachedResponse = MainCache.get<MyRewardListResponseModel>(cacheKey)
        if (cachedResponse != null) {
            emit(Result.success(cachedResponse))
        } else {
            val response = api.getMyRewards(queries = params)
            MainCache.put(cacheKey, response)
            emit(Result.success(response))
        }
    }.catch {
        Log.e("MyRewardDetailService", "getMyRewardDetail: ${it.message}")
        emit(Result.failure(RuntimeException("Something went wrong")))
    }

    suspend fun getFullAddress(
        cityCode: String,
        districtCode: String,
        wardCode: String
    ): Flow<Result<String>> = flow {
        val cityParams: MutableMap<String, Any> = mutableMapOf(
            "ParentCodeFilter" to "0",
            "LevelFilter" to "City"
        )
        val cityCacheKey = generateCacheKey(Endpoints.GET_LOCATIONS, cityParams)
        val cityCachedResponse = MainCache.get<AddressResponseModel>(cityCacheKey)
        val cityResponse: AddressResponseModel = if (cityCachedResponse != null) {
            cityCachedResponse
        } else {
            val response = api.getLocations(queries = cityParams)
            MainCache.put(cityCacheKey, response)
            response
        }
        if ((cityResponse.result?.items ?: listOf()).isEmpty()) {
            emit(Result.failure(RuntimeException("City not found")))
        }
        val city = (cityResponse.result?.items ?: listOf()).find { it.code == cityCode }?.name

        val districtParams: MutableMap<String, Any> = mutableMapOf(
            "ParentCodeFilter" to cityCode,
            "LevelFilter" to "District"
        )
        val districtCacheKey = generateCacheKey(Endpoints.GET_LOCATIONS, districtParams)
        val districtCachedResponse = MainCache.get<AddressResponseModel>(districtCacheKey)
        val districtResponse: AddressResponseModel = if (districtCachedResponse != null) {
            districtCachedResponse
        } else {
            val response = api.getLocations(queries = districtParams)
            MainCache.put(districtCacheKey, response)
            response
        }
        if ((districtResponse.result?.items ?: listOf()).isEmpty()) {
            emit(Result.failure(RuntimeException("District not found")))
        }
        val district =
            (districtResponse.result?.items ?: listOf()).find { it.code == districtCode }?.name

        val wardParams: MutableMap<String, Any> = mutableMapOf(
            "ParentCodeFilter" to districtCode,
            "LevelFilter" to "Ward"
        )
        val wardCacheKey = generateCacheKey(Endpoints.GET_LOCATIONS, wardParams)
        val wardCachedResponse = MainCache.get<AddressResponseModel>(wardCacheKey)
        val wardResponse: AddressResponseModel = if (wardCachedResponse != null) {
            wardCachedResponse
        } else {
            val response = api.getLocations(queries = wardParams)
            MainCache.put(wardCacheKey, response)
            response
        }
        if ((wardResponse.result?.items ?: listOf()).isEmpty()) {
            emit(Result.failure(RuntimeException("City not found")))
        }
        val ward = (wardResponse.result?.items ?: listOf()).find { it.code == wardCode }?.name
        emit(Result.success("$ward, $district, $city"))
    }.catch {
        Log.e("MyRewardDetailService", "getFullAddress: ${it.message}")
        emit(Result.failure(RuntimeException("Something went wrong")))
    }
}