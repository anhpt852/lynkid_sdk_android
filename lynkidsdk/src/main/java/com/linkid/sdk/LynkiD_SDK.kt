package com.linkid.sdk

import android.content.Context
import android.content.Intent

object LynkiD_SDK {
    fun start(context: Context) {
        val intent = Intent(context, LynkiDSDKActivity::class.java)
        context.startActivity(intent)
    }
}