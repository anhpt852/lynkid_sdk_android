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
                "eyJhbGciOiJSUzI1NiIsImtpZCI6ImExODE4ZjQ0ODk0MjI1ZjQ2MWQyMmI1NjA4NDcyMDM3MTc2MGY1OWIiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vdnBpZC11YXQtcGYiLCJhdWQiOiJ2cGlkLXVhdC1wZiIsImF1dGhfdGltZSI6MTcwODMxMjYzNywidXNlcl9pZCI6IjlBUHVBUWpTSG5TNjljQUU3UjNuVVdueFpnbjEiLCJzdWIiOiI5QVB1QVFqU0huUzY5Y0FFN1IzblVXbnhaZ24xIiwiaWF0IjoxNzA5MTExNDIxLCJleHAiOjE3MDkxMTUwMjEsInBob25lX251bWJlciI6Iis4NDg2ODIxODUxNiIsImZpcmViYXNlIjp7ImlkZW50aXRpZXMiOnsicGhvbmUiOlsiKzg0ODY4MjE4NTE2Il19LCJzaWduX2luX3Byb3ZpZGVyIjoiY3VzdG9tIn19.HI4VwHPqDcPE9CJMkrHgweq3PoNkwP-9r5sHJyfymaODJvo4SJeVljaOfDcdte34FjI8mUwsupeuz1UwJ0XuJH3k2bXEMWJGUxvwf8ZrTdRnQ3rxPdJCw0IQmNYxKF37rF4hCYYc46Fu-GvrR15Op6rGeOcZYVb5fEUQ0m8wtX9ZbAjhl8q654dp7naHEibxEnFrSYI1qcKdgJIpempjiI02Fd4HB4XvQcWRfiYppj1qo8UI-1syNB4PiRFEXRHCSEUmenDdndY88SIah16-LLVTSCYSqdYL3_huoV_WtWMvvxgy5r2I2PHmKoMjmPUYNiqe9B2M4VemywbL7-Hm3w",
                "9APuAQjSHnS69cAE7R3nUWnxZgn1"
            )
        }
    }
}