package vn.linkid.sdk.auth.service

import android.util.Log
import vn.linkid.sdk.utils.APIEndpoints
import vn.linkid.sdk.models.auth.ConnectedMemberAuthToken
import vn.linkid.sdk.models.auth.MemberAuthToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class SwitchAccountService(private val api: APIEndpoints) {
    suspend fun switchMember(): Flow<Result<MemberAuthToken>> = flow {
        emit(
            Result.success(
                api.authConnectedMember()
            )
        )
    }.catch {
        Log.e("SwitchAccountService", "switchMember: ${it.message}")
        emit(Result.failure(RuntimeException("Something went wrong")))
    }

    suspend fun createMember(): Flow<Result<ConnectedMemberAuthToken>> = flow {
        emit(
            Result.success(
                api.createMember()
            )
        )
    }.catch {
        Log.e("SwitchAccountService", "createMember: ${it.message}")
        emit(Result.failure(RuntimeException("Something went wrong")))
    }
}