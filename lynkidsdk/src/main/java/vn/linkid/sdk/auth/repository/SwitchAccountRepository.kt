package vn.linkid.sdk.auth.repository

import vn.linkid.sdk.LynkiD_SDK
import vn.linkid.sdk.auth.service.SwitchAccountService
import vn.linkid.sdk.models.auth.ConnectedMember
import vn.linkid.sdk.models.auth.ConnectedMemberAuthToken
import vn.linkid.sdk.models.auth.MemberAuthToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SwitchAccountRepository(private val service: SwitchAccountService) {
    suspend fun switchMember(): Flow<Boolean?> =
        service.switchMember().map { result ->
            if (result.isSuccess) {
                val memberAuthToken: MemberAuthToken? = result.getOrNull()
                if (memberAuthToken != null && memberAuthToken.isSuccess == true) {
                    if (memberAuthToken.data?.newAccessToken != null) {
                        vn.linkid.sdk.LynkiD_SDK.accessToken =
                            memberAuthToken.data.newAccessToken.accessToken ?: ""
                        vn.linkid.sdk.LynkiD_SDK.accessRefreshToken =
                            memberAuthToken.data.newAccessToken.refreshToken ?: ""
                        vn.linkid.sdk.LynkiD_SDK.seedToken =
                            memberAuthToken.data.seedTokenReplacement?.accessToken ?: ""
                        vn.linkid.sdk.LynkiD_SDK.seedRefreshToken =
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
                    vn.linkid.sdk.LynkiD_SDK.memberCode =
                        connectedMemberAuthToken.data?.basicInfo?.memberCode ?: ""
                    if (connectedMemberAuthToken.data?.isExisting == true) {
                        vn.linkid.sdk.LynkiD_SDK.accessToken =
                            connectedMemberAuthToken.data.basicInfo?.accessToken ?: ""
                        vn.linkid.sdk.LynkiD_SDK.accessRefreshToken =
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