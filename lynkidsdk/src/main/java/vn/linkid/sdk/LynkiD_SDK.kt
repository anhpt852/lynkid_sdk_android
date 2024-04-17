package vn.linkid.sdk

import android.content.Context
import android.content.Intent

object LynkiD_SDK {
    var seedToken: String = ""
    var seedRefreshToken: String = ""
    var accessToken: String = ""
    var accessRefreshToken: String = ""
    var memberCode: String = ""
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
        vn.linkid.sdk.LynkiD_SDK.partnerCode = partnerCode
        vn.linkid.sdk.LynkiD_SDK.phoneNumber = phoneNumber
        vn.linkid.sdk.LynkiD_SDK.cif = cif
        vn.linkid.sdk.LynkiD_SDK.name = name
        val intent = Intent(context, vn.linkid.sdk.LynkiDSDKActivity::class.java)
        context.startActivity(intent)
    }
}