package com.linkid.sdk.splash.service

import android.util.Log
import com.linkid.sdk.APIEndpoints
import com.linkid.sdk.models.auth.AuthToken
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
}