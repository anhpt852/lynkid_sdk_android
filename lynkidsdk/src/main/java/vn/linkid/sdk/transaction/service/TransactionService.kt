package vn.linkid.sdk.transaction.service

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import vn.linkid.sdk.LynkiD_SDK
import vn.linkid.sdk.models.transaction.GetTransactionDetailResponseModel
import vn.linkid.sdk.models.transaction.GetTransactionResponseModel
import vn.linkid.sdk.utils.APIEndpoints

class TransactionService(private val api: APIEndpoints) {

    suspend fun getTransactions(index: Int, tab: Int): Flow<Result<GetTransactionResponseModel>> = flow {
        emit(
            Result.success(
                api.getTransactions(
                    queries = mutableMapOf(
                        "OwnerCodeFilter" to LynkiD_SDK.memberCode,
                        "SkipCount" to index * 10,
                        "MaxItem" to 10
                    ))
            )
        )
    }.catch {
        Log.e("TransactionService", "getTransactions: ${it.message}")
        emit(Result.failure(RuntimeException("Something went wrong")))
    }

    suspend fun getTransactionDetail(transactionCode: String): Flow<Result<GetTransactionDetailResponseModel>> = flow {
        emit(
            Result.success(
                api.getTransactionDetail(
                    queries = mutableMapOf(
                        "memberCode" to LynkiD_SDK.memberCode,
                        "tokenTransId" to transactionCode
                    ))
            )
        )
    }.catch {
        Log.e("TransactionService", "getTransactionDetail: ${it.message}")
        emit(Result.failure(RuntimeException("Something went wrong")))
    }



}