package com.linkid.sdk

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
        this.partnerCode = partnerCode
        this.phoneNumber = phoneNumber
        this.cif = cif
        this.name = name
        val intent = Intent(context, LynkiDSDKActivity::class.java)
        context.startActivity(intent)
    }
}