package vn.linkid.sdk.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import vn.linkid.sdk.LynkiD_SDK

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<AppCompatButton>(R.id.btnOpen).setOnClickListener {
            LynkiD_SDK.start(
                this,
                findViewById<AppCompatEditText>(R.id.edtPartnerCode).text.toString(),
                findViewById<AppCompatEditText>(R.id.edtPhoneNumber).text.toString(),
                findViewById<AppCompatEditText>(R.id.edtCIF).text.toString(),
                findViewById<AppCompatEditText>(R.id.edtName).text.toString()
            )
        }
    }
}