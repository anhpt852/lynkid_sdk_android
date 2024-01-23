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
                "eyJhbGciOiJSUzI1NiIsImtpZCI6IjViNjAyZTBjYTFmNDdhOGViZmQxMTYwNGQ5Y2JmMDZmNGQ0NWY4MmIiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vdnBpZC11YXQtcGYiLCJhdWQiOiJ2cGlkLXVhdC1wZiIsImF1dGhfdGltZSI6MTcwNjAyNDg0MywidXNlcl9pZCI6IjlBUHVBUWpTSG5TNjljQUU3UjNuVVdueFpnbjEiLCJzdWIiOiI5QVB1QVFqU0huUzY5Y0FFN1IzblVXbnhaZ24xIiwiaWF0IjoxNzA2MDM0NTI0LCJleHAiOjE3MDYwMzgxMjQsInBob25lX251bWJlciI6Iis4NDg2ODIxODUxNiIsImZpcmViYXNlIjp7ImlkZW50aXRpZXMiOnsicGhvbmUiOlsiKzg0ODY4MjE4NTE2Il19LCJzaWduX2luX3Byb3ZpZGVyIjoiY3VzdG9tIn19.O3E5weCdpeI_zdotRoSrT8TNmLATXqo8aIp4zumsr2KMyaRVwgmM6LEVJhOBwejVaR11Mjtuux1NOZKLhG-pkMc7kWtQd9gUHmxs6sPm-3ezzoHnVaZ867UKA3pHU5Jir6pGdRqluJ1CnePHH-MGN2N1PlRmZkPXjWICjTESeu8D8Xi_bL1Y3EVCHAIu0BRzJdAMFH6iNl3YkoRiyD3HOvQoDo2_R8CEPfUtId0m7P_bVHXjxwSDInqY1Qo_sC2OkVLTBQJTxEiOb0PPwNHklEvwp6fDZmIrTRSzvyp1azQ7Rp8Zu29gvWi_tKihtWZred5-nrOSwj_ieMzjIMj-1g",
                "9APuAQjSHnS69cAE7R3nUWnxZgn1"
            )
        }
    }
}