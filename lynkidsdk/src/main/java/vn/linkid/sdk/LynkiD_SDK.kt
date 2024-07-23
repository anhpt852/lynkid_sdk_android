package vn.linkid.sdk

import android.content.Context
import android.content.Intent
import vn.linkid.sdk.models.auth.ConnectedMember

object LynkiD_SDK {
    var seedToken: String = ""
    var seedRefreshToken: String = ""
    var accessToken: String = ""
    var accessRefreshToken: String = ""
    var isAnonymous: Boolean = false
    var memberCode: String = ""
    var memberId: Int = 0
    var partnerCode: String = ""
    var phoneNumber: String = ""
    var connectedPhoneNumber: String = ""
    var cif: String = ""
    var name: String = ""
    var connectedMember: ConnectedMember? = null
    var appName: String = ""
    fun start(
        context: Context,
        partnerCode: String,
        phoneNumber: String,
        cif: String,
        name: String,
        appName: String,
    ) {
        LynkiD_SDK.partnerCode = partnerCode
        LynkiD_SDK.phoneNumber = phoneNumber
        LynkiD_SDK.cif = cif
        LynkiD_SDK.name = name
        LynkiD_SDK.appName = appName
        val intent = Intent(context, LynkiDSDKActivity::class.java)
        context.startActivity(intent)
    }
}