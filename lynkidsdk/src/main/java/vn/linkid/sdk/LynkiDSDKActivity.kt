package vn.linkid.sdk

import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import vn.linkid.sdk.databinding.ActivityMainSdkBinding

class LynkiDSDKActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainSdkBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainSdkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("LynkiDSDKActivity", "onCreate")


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
            @Suppress("DEPRECATION")
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            @Suppress("DEPRECATION")
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)

        }
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT

        setUpNavController()
    }


    private fun setUpNavController() {
        binding.apply {
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            val navController = navHostFragment.navController
            val menu = bottomNavigation.menu
            val middleItem = menu.getItem(1)
            middleItem.isEnabled = false
            bottomNavigation.setupWithNavController(navController)
            navController.addOnDestinationChangedListener { _, destination, _ ->
                if (destination.id == R.id.homeFragment || destination.id == R.id.myRewardFragment) {
                    bottomNavigation.visibility = View.VISIBLE
                } else {
                    bottomNavigation.visibility = View.GONE
                }
//                if (destination.id == R.id.homeFragment || destination.id == R.id.settingFragment || destination.id == R.id.introFragment) {
//                    window.navigationBarColor = Color.WHITE
//                } else {
//                    window.navigationBarColor = Color.parseColor("#F7F7F7")
//                }
//                if (destination.id == R.id.homeFragment || destination.id == R.id.languageFragment) {
//                    window.statusBarColor = Color.parseColor("#1A998E")
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                        window.insetsController?.setSystemBarsAppearance(
//                            0, WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
//                        )
//                    } else {
//                        @Suppress("DEPRECATION")
//                        window.decorView.systemUiVisibility = 0
//                    }
//                } else {
//                    window.statusBarColor = Color.WHITE
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                        window.insetsController?.setSystemBarsAppearance(
//                            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
//                            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
//                        )
//                    } else {
//                        @Suppress("DEPRECATION")
//                        window.decorView.systemUiVisibility =
//                            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//                    }
//                }
            }
        }
    }


    override fun getResources(): Resources {
        val res = super.getResources()
        val config = Configuration(res.configuration)
        if (config.fontScale != 1.0f) {
            config.fontScale = 1.0f
            return createConfigurationContext(config).resources
        }
        return res
    }
}