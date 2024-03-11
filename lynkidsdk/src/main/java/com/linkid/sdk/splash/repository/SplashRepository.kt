package com.linkid.sdk.splash.repository

import com.linkid.sdk.models.auth.AuthToken
import com.linkid.sdk.splash.service.SplashService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SplashRepository(private val service: SplashService) {

    suspend fun generateToken(): Flow<Result<AuthToken>> =
        service.generateToken().map { result ->
            if (result.isSuccess) {
                val authToken: AuthToken? = result.getOrNull()
                if (authToken != null && authToken.isSuccess) {
                    Result.success(authToken)
                } else {
                    Result.failure(result.exceptionOrNull()!!)
                }
            } else {
                Result.failure(result.exceptionOrNull()!!)
            }
        }
}