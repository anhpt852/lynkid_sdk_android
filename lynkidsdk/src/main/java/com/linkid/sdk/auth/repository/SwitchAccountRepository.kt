package com.linkid.sdk.auth.repository

import com.linkid.sdk.LynkiD_SDK
import com.linkid.sdk.auth.service.SwitchAccountService
import com.linkid.sdk.models.auth.ConnectedMember
import com.linkid.sdk.models.auth.ConnectedMemberAuthToken
import com.linkid.sdk.models.auth.MemberAuthToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SwitchAccountRepository(private val service: SwitchAccountService) {
    suspend fun switchMember(): Flow<Boolean?> =
        service.switchMember().map { result ->
            if (result.isSuccess) {
                val memberAuthToken: MemberAuthToken? = result.getOrNull()
                if (memberAuthToken != null && memberAuthToken.isSuccess == true) {
                    if (memberAuthToken.data?.newAccessToken != null) {
                        LynkiD_SDK.accessToken =
                            memberAuthToken.data.newAccessToken.accessToken ?: ""
                        LynkiD_SDK.accessRefreshToken =
                            memberAuthToken.data.newAccessToken.refreshToken ?: ""
                        LynkiD_SDK.seedToken =
                            memberAuthToken.data.seedTokenReplacement?.accessToken ?: ""
                        LynkiD_SDK.seedRefreshToken =
                            memberAuthToken.data.seedTokenReplacement?.refreshToken ?: ""
                    }
                    true
                } else {
                    false
                }
            } else {
                false
            }
        }

    suspend fun createMember(): Flow<ConnectedMember?> =
        service.createMember().map { result ->
            if (result.isSuccess) {
                val connectedMemberAuthToken: ConnectedMemberAuthToken? = result.getOrNull()
                if (connectedMemberAuthToken != null && connectedMemberAuthToken.isSuccess) {
                    LynkiD_SDK.memberCode =
                        connectedMemberAuthToken.data?.basicInfo?.memberCode ?: ""
                    if (connectedMemberAuthToken.data?.isExisting == true) {
                        LynkiD_SDK.accessToken =
                            connectedMemberAuthToken.data.basicInfo?.accessToken ?: ""
                        LynkiD_SDK.accessRefreshToken =
                            connectedMemberAuthToken.data.basicInfo?.refreshToken ?: ""
                    }
                    connectedMemberAuthToken.data
                } else {
                    null
                }
            } else {
                null
            }
        }
}