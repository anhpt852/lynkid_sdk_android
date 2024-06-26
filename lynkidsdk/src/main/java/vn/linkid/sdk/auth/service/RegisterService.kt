package vn.linkid.sdk.auth.service

import android.util.Log
import vn.linkid.sdk.utils.APIEndpoints
import vn.linkid.sdk.models.auth.ConnectedMemberAuthToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class RegisterService(private val api: APIEndpoints) {
    suspend fun createMember(): Flow<Result<ConnectedMemberAuthToken>> = flow {
        emit(
            Result.success(
                api.createMember()
            )
        )
    }.catch {
        Log.e("RegisterService", "createMember: ${it.message}")
        emit(Result.failure(RuntimeException("Something went wrong")))
    }
}