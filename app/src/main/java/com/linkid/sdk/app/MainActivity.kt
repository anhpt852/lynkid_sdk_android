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
                "eyJhbGciOiJSUzI1NiIsImtpZCI6ImFlYzU4NjcwNGNhOTZiZDcwMzZiMmYwZDI4MGY5NDlmM2E5NzZkMzgiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vdnBpZC11YXQtcGYiLCJhdWQiOiJ2cGlkLXVhdC1wZiIsImF1dGhfdGltZSI6MTcwODMxMjYzNywidXNlcl9pZCI6IjlBUHVBUWpTSG5TNjljQUU3UjNuVVdueFpnbjEiLCJzdWIiOiI5QVB1QVFqU0huUzY5Y0FFN1IzblVXbnhaZ24xIiwiaWF0IjoxNzA4NTA0NjgwLCJleHAiOjE3MDg1MDgyODAsInBob25lX251bWJlciI6Iis4NDg2ODIxODUxNiIsImZpcmViYXNlIjp7ImlkZW50aXRpZXMiOnsicGhvbmUiOlsiKzg0ODY4MjE4NTE2Il19LCJzaWduX2luX3Byb3ZpZGVyIjoiY3VzdG9tIn19.Ol-YTlLCbY-KYXpaIcksj3ZRz2o3XX5n-3IAuq8ZESxfmiUS3kqlKw_mzXhswFkIIEOphwYi5d54FKmCK3GHuX3m--w8TZu0deeO_1Wjm60rPigFbfjWlva91NpqsyNWlUiOy3TafSrv_57p0JDiCmcYWN0-DoEpz8i9jai4oO7f0STJDKL2CXGzrU-ecG-Otn0l45ZW69a1vrS9pVcE8xwsXL_5BbewenVIP9zAJLFWPAgDHHTFP1Q1VZmu0YUcAc94QGAKOf3wQ9vhLWOFQLI7WjUR9mCR0LCPMvWdbc3JZ3yFcJdR1G6t93yQsWf-Cr5VmZ845Dq9r6wYM7z0rw",
                "9APuAQjSHnS69cAE7R3nUWnxZgn1"
            )
        }
    }
}