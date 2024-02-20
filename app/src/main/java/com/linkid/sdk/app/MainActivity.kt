package com.linkid.sdk.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.linkid.sdk.LynkiD_SDK

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.btnOpenSDK).setOnClickListener {
            LynkiD_SDK.start(
                this,
                "eyJhbGciOiJSUzI1NiIsImtpZCI6ImFlYzU4NjcwNGNhOTZiZDcwMzZiMmYwZDI4MGY5NDlmM2E5NzZkMzgiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vdnBpZC11YXQtcGYiLCJhdWQiOiJ2cGlkLXVhdC1wZiIsImF1dGhfdGltZSI6MTcwODMxMjYzNywidXNlcl9pZCI6IjlBUHVBUWpTSG5TNjljQUU3UjNuVVdueFpnbjEiLCJzdWIiOiI5QVB1QVFqU0huUzY5Y0FFN1IzblVXbnhaZ24xIiwiaWF0IjoxNzA4MzEyNjM3LCJleHAiOjE3MDgzMTYyMzcsInBob25lX251bWJlciI6Iis4NDg2ODIxODUxNiIsImZpcmViYXNlIjp7ImlkZW50aXRpZXMiOnsicGhvbmUiOlsiKzg0ODY4MjE4NTE2Il19LCJzaWduX2luX3Byb3ZpZGVyIjoiY3VzdG9tIn19.A5oz1TX4VoigluRsej0tJVSU_NCj8OkaM8jF4XSUzCH72DhMsn71x1iAcMO4yRxq2f-hjxZ4z9HvHrSlQ859toBkwTs2izNNBzAe-d43BMod1LqehldOuD-aGQjbY3BgXSCS2o854V6xqfxQH-H0iAhXTvnXD9X2DqC0lU_NBynRymDQZL_Qt38g0ATlAoVi7pOYQ4nVJ8JqcVJWL4y52-1_U1B_nEOkL_apoEsqiGYz8VZSin-xioSw_LBdJHwqnZz-zpwVT8-Nu5igo_CzclkMFnyOVpE34ooS4fp6TBlmGhweOQuhTmyKczMF-sSEJ8Fqn_KyPkHA3c_-8XN5Tw",
                "9APuAQjSHnS69cAE7R3nUWnxZgn1"
            )
        }
    }
}