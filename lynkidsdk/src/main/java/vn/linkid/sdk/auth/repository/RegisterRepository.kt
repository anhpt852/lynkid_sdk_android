package vn.linkid.sdk.auth.repository

import vn.linkid.sdk.LynkiD_SDK
import vn.linkid.sdk.auth.service.RegisterService
import vn.linkid.sdk.models.auth.ConnectedMember
import vn.linkid.sdk.models.auth.ConnectedMemberAuthToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RegisterRepository(private val service: RegisterService) {
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