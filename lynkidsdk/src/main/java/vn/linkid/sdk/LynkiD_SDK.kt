package vn.linkid.sdk

import android.content.Context
import android.content.Intent

object LynkiD_SDK {
    var seedToken: String = ""
    var seedRefreshToken: String = ""
    var accessToken: String = ""
    var accessRefreshToken: String = ""
    var memberCode: String = ""
    var memberId: Int = 0
    var partnerCode: String = ""
    var phoneNumber: String = ""
    var connectedPhoneNumber: String = ""
    var cif: String = ""
    var name: String = ""
    fun start(
        context: Context,
        partnerCode: String,
        phoneNumber: String,
        cif: String,
        name: String
    ) {
        LynkiD_SDK.partnerCode = partnerCode
        LynkiD_SDK.phoneNumber = phoneNumber
        LynkiD_SDK.cif = cif
        LynkiD_SDK.name = name
        val intent = Intent(context, LynkiDSDKActivity::class.java)
        context.startActivity(intent)
    }
}