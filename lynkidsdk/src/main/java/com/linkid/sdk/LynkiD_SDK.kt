package com.linkid.sdk

import android.content.Context
import android.content.Intent

object LynkiD_SDK {
    lateinit var token: String
    lateinit var memberCode: String
    lateinit var partnerCode: String
    lateinit var phoneNumber: String
    lateinit var cif: String
    lateinit var name: String
    fun start(context: Context, partnerCode: String, phoneNumber: String, cif: String, name: String) {
        this.partnerCode = partnerCode
        this.phoneNumber = phoneNumber
        this.cif = cif
        this.name = name
        val intent = Intent(context, LynkiDSDKActivity::class.java)
        context.startActivity(intent)
    }
}