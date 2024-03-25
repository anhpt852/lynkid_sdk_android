package com.linkid.sdk.splash.repository

import android.util.Log
import com.linkid.sdk.LynkiD_SDK
import com.linkid.sdk.models.auth.AuthToken
import com.linkid.sdk.models.auth.AuthType
import com.linkid.sdk.models.auth.ConnectedMemberAuthToken
import com.linkid.sdk.splash.service.SplashService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class SplashRepository(private val service: SplashService) {

    suspend fun generateToken(): Flow<AuthType> =
        service.generateToken().flatMapConcat { result ->
            if (result.isSuccess) {
                val authToken: AuthToken? = result.getOrNull()
                if (authToken != null && authToken.isSuccess) {
                    Log.d("SplashRepository", "generateToken: ${authToken.seedToken}/ LynkiD_SDK.seedToken ${LynkiD_SDK.seedToken}")
                    LynkiD_SDK.seedToken = authToken.seedToken
                    Log.d("SplashRepository", "generateToken: LynkiD_SDK.seedToken ${LynkiD_SDK.seedToken}")
                    checkMember()
                } else {
                    flowOf(AuthType.ANONYMOUS)
                }
            } else {
                flowOf(AuthType.ANONYMOUS)
            }
        }

    private suspend fun checkMember(): Flow<AuthType> =
        service.checkMember().map { result ->
            if (result.isSuccess) {
                val connectedMemberAuthToken: ConnectedMemberAuthToken? = result.getOrNull()
                if (connectedMemberAuthToken != null && connectedMemberAuthToken.isSuccess) {
                    LynkiD_SDK.memberCode = connectedMemberAuthToken.data?.basicInfo?.memberCode ?: ""
                    if (connectedMemberAuthToken.data?.isExisting == true) {
                        LynkiD_SDK.accessToken = connectedMemberAuthToken.data.basicInfo?.accessToken ?: ""
                        if (connectedMemberAuthToken.data.connectionInfo?.isExisting == true) {
                            AuthType.CONNECTED_MEMBER
                        } else if (connectedMemberAuthToken.data.connectionInfo?.isExisting == false) {
                            AuthType.NON_CONNECTED_MEMBER
                        }
                    } else {
                        if (connectedMemberAuthToken.data?.connectionInfo?.isExisting == true) {
                            AuthType.CONNECTED_NON_MEMBER
                        } else if (connectedMemberAuthToken.data?.connectionInfo?.isExisting == false) {
                            AuthType.NON_CONNECTED_NON_MEMBER
                        }
                    }
                    AuthType.ANONYMOUS
                } else {
                    AuthType.ANONYMOUS
                }
            } else {
                AuthType.ANONYMOUS
            }
        }
}