package vn.linkid.sdk.imedia.service

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import vn.linkid.sdk.LynkiD_SDK
import vn.linkid.sdk.cache.MainCache
import vn.linkid.sdk.models.gift.GiftGroupResponseModel
import vn.linkid.sdk.models.imedia.GetIMediaGiftsResponseModel
import vn.linkid.sdk.models.imedia.GetIMediaHistoryResponseModel
import vn.linkid.sdk.models.imedia.GetThirdPartyBrandByVendorResponseModel
import vn.linkid.sdk.utils.APIEndpoints
import vn.linkid.sdk.utils.Endpoints
import vn.linkid.sdk.utils.generateCacheKey

class IMediaService(private val api: APIEndpoints) {

    suspend fun getBrandByVendor(): Flow<Result<GetThirdPartyBrandByVendorResponseModel>> = flow {
        val params: MutableMap<String, Any> = mutableMapOf(
            "MemberCode" to LynkiD_SDK.memberCode,
            "SkipCount" to 0,
            "MaxResultCount" to 1000
        )
        val cacheKey = generateCacheKey(Endpoints.GET_ALL_THIRD_PARTY_BRAND_BY_VENDOR_TYPE, params)
        val cachedResponse = MainCache.get<GetThirdPartyBrandByVendorResponseModel>(cacheKey)
        if (cachedResponse != null) {
            emit(Result.success(cachedResponse))
        } else {
            val response = api.getBrandByVendor(queries = params)
            MainCache.put(cacheKey, response)
            emit(Result.success(response))
        }
    }.catch {
        Log.e("iMediaService", "getBrandByVendor: ${it.message}")
        emit(Result.failure(RuntimeException("Something went wrong")))
    }

    suspend fun getGiftsByGroupType(groupType: Int): Flow<Result<GiftGroupResponseModel>> = flow {
        val params: MutableMap<String, Any> = mutableMapOf(
            "MemberCode" to LynkiD_SDK.memberCode,
            "GroupTypeFilter" to groupType,
            "SkipCount" to 0,
            "MaxResultCount" to 100
        )
        val cacheKey = generateCacheKey(Endpoints.GET_GIFTS_BY_GROUP_TYPE, params)
        val cachedResponse = MainCache.get<GiftGroupResponseModel>(cacheKey)
        if (cachedResponse != null) {
            emit(Result.success(cachedResponse))
        } else {
            val response = api.getGiftsByGroupType(queries = params)
            MainCache.put(cacheKey, response)
            emit(Result.success(response))
        }
    }.catch {
        Log.e("iMediaService", "getGiftsByGroupType: ${it.message}")
        emit(Result.failure(RuntimeException("Something went wrong")))
    }

    suspend fun getAllIMedia(brandId: Int): Flow<Result<GetIMediaGiftsResponseModel>> = flow {
        val params: MutableMap<String, Any> = mutableMapOf(
            "MemberCode" to LynkiD_SDK.memberCode,
            "SkipCount" to 0,
            "MaxResultCount" to 100,
            "MaxItem" to 5,
            "GiftTypeFilter" to "TopupPhone",
            "Sorting" to "RequiredCoin",
            "BrandIdFilter" to brandId
        )
        val cacheKey = generateCacheKey(Endpoints.GET_ALL_EFFECTIVE_CATEGORY_TOPUP, params)
        val cachedResponse = MainCache.get<GetIMediaGiftsResponseModel>(cacheKey)
        if (cachedResponse != null) {
            emit(Result.success(cachedResponse))
        } else {
            val response = api.getAllIMedia(queries = params)
            MainCache.put(cacheKey, response)
            emit(Result.success(response))
        }
    }.catch {
        Log.e("iMediaService", "getAllIMedia: ${it.message}")
        emit(Result.failure(RuntimeException("Something went wrong")))
    }

    suspend fun getIMediaHistory(
        index: Int,
        tab: Int
    ): Flow<Result<GetIMediaHistoryResponseModel>> = flow {
        val params: MutableMap<String, Any> = mutableMapOf(
            "MemberCode" to LynkiD_SDK.memberCode,
            "SkipCount" to index * 10,
            "MaxResultCount" to 10,
            "MaxItem" to 10,
            "StatusFilter" to "Delivered",
            "EgiftStatusFilter" to "R"
        )
        val cacheKey = generateCacheKey(Endpoints.GET_IMEDIA_HISTORY, params)
        val cachedResponse = MainCache.get<GetIMediaHistoryResponseModel>(cacheKey)
        if (cachedResponse != null) {
            emit(Result.success(cachedResponse))
        } else {
            val response = api.getIMediaHistory(queries = params)
            MainCache.put(cacheKey, response)
            emit(Result.success(response))
        }
    }.catch {
        Log.e("iMediaService", "getIMediaHistory: ${it.message}")
        emit(Result.failure(RuntimeException("Something went wrong")))
    }

}