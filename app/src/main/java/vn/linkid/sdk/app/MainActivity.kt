package vn.linkid.sdk.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import vn.linkid.sdk.LynkiD_SDK

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.btnOpenSDK).setOnClickListener {
            LynkiD_SDK.start(
                this,
                "218",
                "+84868218516",
                "111111",
                "PT"
            )
        }
    }
}