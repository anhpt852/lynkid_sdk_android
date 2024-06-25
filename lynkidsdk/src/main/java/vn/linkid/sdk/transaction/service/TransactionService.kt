package vn.linkid.sdk.transaction.service

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import vn.linkid.sdk.LynkiD_SDK
import vn.linkid.sdk.cache.MainCache
import vn.linkid.sdk.models.merchant.GetMerchantResponseModel
import vn.linkid.sdk.models.transaction.GetTransactionDetailResponseModel
import vn.linkid.sdk.models.transaction.GetTransactionItem
import vn.linkid.sdk.models.transaction.GetTransactionResponseModel
import vn.linkid.sdk.utils.APIEndpoints
import vn.linkid.sdk.utils.Endpoints
import vn.linkid.sdk.utils.generateCacheKey

class TransactionService(private val api: APIEndpoints) {

    suspend fun getTransactions(index: Int, tab: Int): Flow<Result<GetTransactionResponseModel>> =
        flow {
            val actionTypeFilter = getActionTypeFilter(tab) ?: ""
            val params = mutableMapOf<String, Any>(
                "NationalId" to LynkiD_SDK.memberCode,
                "SkipCount" to index * 10,
                "MaxItem" to 10
            )
            if (actionTypeFilter.isNotEmpty()) params["ActionTypeFilter"] = actionTypeFilter
            val cacheKey = generateCacheKey(Endpoints.GET_TRANSACTIONS, params)
            val cachedResponse = MainCache.get<GetTransactionResponseModel>(cacheKey)
            if (cachedResponse != null) {
                emit(Result.success(cachedResponse))
            } else {
                val response = api.getTransactions(queries = params)
                MainCache.put(cacheKey, response)
                emit(Result.success(response))
            }
        }.catch {
            Log.e("TransactionService", "getTransactions: ${it.message}")
            emit(Result.failure(RuntimeException("Something went wrong")))
        }

    suspend fun getSingleTransactionByOrderCode(orderCode: String): Flow<Result<GetTransactionResponseModel>> =
        flow {
            val params = mutableMapOf<String, Any>(
                "NationalId" to LynkiD_SDK.memberCode,
                "SkipCount" to 0,
                "MaxItem" to 1,
                "OrderCodeFilter" to orderCode
            )
            val cacheKey = generateCacheKey(Endpoints.GET_TRANSACTIONS, params)
            val cachedResponse = MainCache.get<GetTransactionResponseModel>(cacheKey)
            if (cachedResponse != null) {
                emit(Result.success(cachedResponse))
            } else {
                val response = api.getTransactions(queries = params)
                MainCache.put(cacheKey, response)
                emit(Result.success(response))
            }
        }.catch {
            Log.e("TransactionService", "getTransactions: ${it.message}")
            emit(Result.failure(RuntimeException("Something went wrong")))
        }

    private fun getActionTypeFilter(tab: Int): String {
        return when (tab) {
            0 -> ""
            1 -> "PayByToken;Redeem;CashedOut;CashOutFee"
            2 -> "Action;BatchManualGrant;Order;SingleManualGrant;Topup"
            3 -> "Exchange;ExchangeAndUse;RevertExchange"
            else -> ""
        }
    }

    suspend fun getTransactionDetail(transactionCode: String): Flow<Result<GetTransactionDetailResponseModel>> =
        flow {
            val params: MutableMap<String, Any> = mutableMapOf(
                "memberCode" to LynkiD_SDK.memberCode,
                "tokenTransId" to transactionCode
            )
            val cacheKey = generateCacheKey(Endpoints.GET_TRANSACTION_DETAIL, params)
            val cachedResponse = MainCache.get<GetTransactionDetailResponseModel>(cacheKey)
            if (cachedResponse != null) {
                emit(Result.success(cachedResponse))
            } else {
                val response = api.getTransactionDetail(queries = params)
                MainCache.put(cacheKey, response)
                emit(Result.success(response))
            }
        }.catch {
            Log.e("TransactionService", "getTransactionDetail: ${it.message}")
            emit(Result.failure(RuntimeException("Something went wrong")))
        }



    suspend fun getMerchant(): Flow<Result<GetMerchantResponseModel>> =
        flow {
            val cacheKey = Endpoints.GET_MERCHANT
            val cachedResponse = MainCache.get<GetMerchantResponseModel>(cacheKey)
            if (cachedResponse != null) {
                emit(Result.success(cachedResponse))
            } else {
                val response = api.getMerchant()
                MainCache.put(cacheKey, response)
                emit(Result.success(response))
            }
        }.catch {
            Log.e("TransactionService", "getTransactions: ${it.message}")
            emit(Result.failure(RuntimeException("Something went wrong")))
        }


}