package vn.linkid.sdk.gift_detail.service

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import vn.linkid.sdk.utils.APIEndpoints
import vn.linkid.sdk.utils.Endpoints
import vn.linkid.sdk.LynkiD_SDK
import vn.linkid.sdk.cache.MainCache
import vn.linkid.sdk.utils.generateCacheKey
import vn.linkid.sdk.models.exchange.ExchangeResponseModel
import vn.linkid.sdk.models.gift.GiftDetailResponseModel
import vn.linkid.sdk.models.point.PointResponseModel

class GiftDetailService(private val api: APIEndpoints) {

    suspend fun getGiftDetail(id: Int): Flow<Result<GiftDetailResponseModel>> = flow {
        val params: MutableMap<String, Any> = mutableMapOf(
            "MemberCode" to LynkiD_SDK.memberCode,
            "GiftId" to id,
            "MaxItem" to 1
        )
        val cacheKey = generateCacheKey(Endpoints.GET_GIFT_DETAILS, params)
        val cachedResponse = MainCache.get<GiftDetailResponseModel>(cacheKey)
        if (cachedResponse != null) {
            emit(Result.success(cachedResponse))
        } else {
            val response = api.getGiftDetails(queries = params)
            MainCache.put(cacheKey, response)
            emit(Result.success(response))
        }
    }.catch {
        Log.e("GiftDetailService", "getGiftDetail: ${it.message}")
        emit(Result.failure(RuntimeException("Something went wrong")))
    }

    suspend fun getPointInfo(): Flow<Result<PointResponseModel>> = flow {
        val cacheKey = Endpoints.GET_POINT_INFO
        val cachedResponse = MainCache.get<PointResponseModel>(cacheKey)
        if (cachedResponse != null) {
            emit(Result.success(cachedResponse))
        } else {
            val response = api.getPointInfo()
            MainCache.put(cacheKey, response)
            emit(Result.success(response))
        }
    }.catch {
        Log.e("GiftDetailService", "getPointInfo: ${it.message}")
        emit(Result.failure(RuntimeException("Something went wrong")))
    }

    suspend fun createTransaction(
        giftCode: String,
        quantity: Int,
        totalAmount: Double
    ): Flow<Result<Pair<String, ExchangeResponseModel>>> = flow {
        val session = "LynkiD_SDK.session"
        val params: MutableMap<String, Any> = mutableMapOf(
            "cifCode" to LynkiD_SDK.cif,
            "giftCode" to giftCode,
            "quantity" to quantity,
            "totalAmount" to totalAmount
        )
        val response = api.createTransaction(body = params)
        emit(Result.success(Pair(session, response)))
    }.catch {
        Log.e("GiftDetailService", "createTransaction: ${it.message}")
        emit(Result.failure(RuntimeException("Something went wrong")))
    }

}