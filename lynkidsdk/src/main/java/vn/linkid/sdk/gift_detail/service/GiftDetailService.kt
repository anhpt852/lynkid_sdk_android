package vn.linkid.sdk.gift_detail.service

import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import vn.linkid.sdk.utils.APIEndpoints
import vn.linkid.sdk.utils.Endpoints
import vn.linkid.sdk.LynkiD_SDK
import vn.linkid.sdk.cache.MainCache
import vn.linkid.sdk.models.common.ErrorResponse
import vn.linkid.sdk.models.diamond.GetDiamondMemberInfoResponseModel
import vn.linkid.sdk.utils.generateCacheKey
import vn.linkid.sdk.models.exchange.ExchangeResponseModel
import vn.linkid.sdk.models.flash_sale.GetAllFlashSaleProgramResponseModel
import vn.linkid.sdk.models.gift.GiftDetailResponseModel
import vn.linkid.sdk.models.imedia.TopupRedeemInfo
import vn.linkid.sdk.models.imedia.toJsonStringForExchange
import vn.linkid.sdk.models.point.PointResponseModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

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
        sessionId: String,
        giftCode: String,
        quantity: Int,
        totalAmount: Double,
        description: String,
        topupRedeemInfo: TopupRedeemInfo? = null
    ): Flow<Result<Pair<String, ExchangeResponseModel>>> = flow {
        val dateFormat =
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }
        val params: MutableMap<String, Any> = mutableMapOf(
            "memberCode" to LynkiD_SDK.memberCode,
            "cifCode" to LynkiD_SDK.cif,
            "sessionId" to sessionId,
            "giftCode" to giftCode,
            "quantity" to quantity,
            "totalAmount" to totalAmount,
            "date" to dateFormat.format(Date())
        )
        if (description.isNotEmpty()) {
            params["description"] = description
        } else if (topupRedeemInfo != null) {
            params["description"] = topupRedeemInfo.toJsonStringForExchange()
        }
        val response = api.createTransaction(body = params)
        emit(Result.success(Pair(sessionId, response)))
    }.catch { throwable ->
        val errorMessage = when (throwable) {
            is HttpException -> {
                val errorBody = throwable.response()?.errorBody()?.string()
                try {
                    Log.d("GiftDetailService", "createTransaction1: $errorBody")
                    val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                    errorResponse.message
                } catch (e: Exception) {
                    Log.d("GiftDetailService", "createTransaction2: $e")
                    errorBody ?: "Unknown error occurred"
                }
            }

            else -> throwable.message ?: "Unknown error occurred"
        }
        Log.e("GiftDetailService", "createTransaction: $errorMessage")
        emit(Result.failure(RuntimeException(errorMessage)))
    }

    suspend fun confirmTransaction(
        sessionId: String,
        otpCode: String,
    ): Flow<Result<Pair<String, ExchangeResponseModel>>> = flow {
        val params: MutableMap<String, Any> = mutableMapOf(
            "memberCode" to LynkiD_SDK.memberCode,
            "cifCode" to LynkiD_SDK.cif,
            "sessionId" to sessionId,
            "otpCode" to otpCode
        )
        val response = api.confirmTransaction(body = params)
        emit(Result.success(Pair(sessionId, response)))
    }.catch {
        Log.e("GiftDetailService", "confirmTransaction: ${it.message}")
        emit(Result.failure(RuntimeException("Something went wrong")))
    }

    suspend fun getAllFlashSaleProgram(): Flow<Result<GetAllFlashSaleProgramResponseModel>> = flow {
        val params: MutableMap<String, Any> = mutableMapOf(
            "MemberCode" to LynkiD_SDK.memberCode,
            "MaxItemFlashSaleProgram" to 1,
            "MaxItemGiftFlashSale" to 0
        )
        val cacheKey = generateCacheKey(Endpoints.GET_ALL_FLASH_SALE_PROGRAM, params)
        val cachedResponse = MainCache.get<GetAllFlashSaleProgramResponseModel>(cacheKey)
        if (cachedResponse != null) {
            emit(Result.success(cachedResponse))
        } else {
            val response = api.getAllFlashSaleProgram(queries = params)
            MainCache.put(cacheKey, response)
            emit(Result.success(response))
        }
    }.catch {
        Log.e("GiftDetailService", "getAllFlashSaleProgram: ${it.message}")
        emit(Result.failure(RuntimeException("Something went wrong")))
    }

    suspend fun getDiamondMemberInfo(): Flow<Result<GetDiamondMemberInfoResponseModel>> = flow {
        val params: MutableMap<String, Any> = mutableMapOf(
            "MemberCode" to LynkiD_SDK.memberCode,
            "MemberId" to LynkiD_SDK.memberId
        )
        val cacheKey = generateCacheKey(Endpoints.GET_DIAMOND_MEMBER_INFO, params)
        val cachedResponse = MainCache.get<GetDiamondMemberInfoResponseModel>(cacheKey)
        if (cachedResponse != null) {
            emit(Result.success(cachedResponse))
        } else {
            val response = api.getDiamondMemberInfo(queries = params)
            MainCache.put(cacheKey, response)
            emit(Result.success(response))
        }
    }.catch {
        Log.e("GiftDetailService", "getDiamondMemberInfo: ${it.message}")
        emit(Result.failure(RuntimeException("Something went wrong")))
    }

}