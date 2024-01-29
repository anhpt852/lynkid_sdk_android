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
                "eyJhbGciOiJSUzI1NiIsImtpZCI6IjViNjAyZTBjYTFmNDdhOGViZmQxMTYwNGQ5Y2JmMDZmNGQ0NWY4MmIiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vdnBpZC11YXQtcGYiLCJhdWQiOiJ2cGlkLXVhdC1wZiIsImF1dGhfdGltZSI6MTcwNjUxNzYyNywidXNlcl9pZCI6IjlBUHVBUWpTSG5TNjljQUU3UjNuVVdueFpnbjEiLCJzdWIiOiI5QVB1QVFqU0huUzY5Y0FFN1IzblVXbnhaZ24xIiwiaWF0IjoxNzA2NTE3NjI3LCJleHAiOjE3MDY1MjEyMjcsInBob25lX251bWJlciI6Iis4NDg2ODIxODUxNiIsImZpcmViYXNlIjp7ImlkZW50aXRpZXMiOnsicGhvbmUiOlsiKzg0ODY4MjE4NTE2Il19LCJzaWduX2luX3Byb3ZpZGVyIjoiY3VzdG9tIn19.YpIv5NxOkoC8NWvPZqHvHkm9kPUDOl_f1tr3NmMPPicYIevTtb1tR8m_WbKyq4tYwVB3KY19QPwTpDF5oi0e3_CLc7SwM72uU--zSn9fe8nzWKOVz1jBcAPMHJ1FBflM5iwk8ZVdd7UT36RKZOmwQkLvVl_2xeYk6WzVSbm7_g4wRUBK6PnintIBLLox3aRfen7NsDljgH3Ff3abAJ1WXFk4Y4hrWsAaYK9KZj09blGey_BQaSnD9BSQy2nqOHRf7To4XIZ56k-7ZgmDnpDSLPcCzC7bEbinwslUeN_Nr5CGDubJMjxukcggwSnOtw6TJmgiG41dh-50CQlXBBiB7A",
                "9APuAQjSHnS69cAE7R3nUWnxZgn1"
            )
        }
    }
}