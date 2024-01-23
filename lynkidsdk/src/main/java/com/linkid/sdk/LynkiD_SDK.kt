package com.linkid.sdk

import android.content.Context
import android.content.Intent

object LynkiD_SDK {
    lateinit var token: String
    lateinit var memberCode: String
    fun start(context: Context, token: String, memberCode: String) {
        this.token = token
        this.memberCode = memberCode
        val intent = Intent(context, LynkiDSDKActivity::class.java)
        context.startActivity(intent)
    }
}