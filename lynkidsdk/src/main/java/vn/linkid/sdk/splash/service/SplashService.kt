package vn.linkid.sdk.splash.service

import android.util.Log
import vn.linkid.sdk.APIEndpoints
import vn.linkid.sdk.models.auth.AuthToken
import vn.linkid.sdk.models.auth.ConnectedMemberAuthToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class SplashService(private val api: APIEndpoints) {

    suspend fun generateToken(): Flow<Result<AuthToken>> = flow {
        emit(
            Result.success(
                api.generateToken()
            )
        )
    }.catch {
        Log.e("SplashService", "generateToken: ${it.message}")
        emit(Result.failure(RuntimeException("Something went wrong")))
    }

    suspend fun checkMember(): Flow<Result<ConnectedMemberAuthToken>> = flow {
        emit(
            Result.success(
                api.checkMember()
            )
        )
    }.catch {
        Log.e("SplashService", "checkMember: ${it.message}")
        emit(Result.failure(RuntimeException("Something went wrong")))
    }
}