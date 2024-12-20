package vn.linkid.sdk

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import vn.linkid.sdk.databinding.ActivityMainSdkBinding
import vn.linkid.sdk.imedia.ui.IMediaHistoryFragmentDirections
import vn.linkid.sdk.imedia.ui.IMediaHomeFragmentDirections
import vn.linkid.sdk.models.imedia.TopupRedeemInfo
import vn.linkid.sdk.my_reward.ui.MyRewardFragmentDirections
import vn.linkid.sdk.transaction.ui.TransactionFragmentDirections
import vn.linkid.sdk.transaction.ui.TransactionListFragmentDirections

class LynkiDSDKActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainSdkBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainContext = this
        binding = ActivityMainSdkBinding.inflate(layoutInflater)
        setContentView(binding.root)


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
            bottomNavigation.setupWithNavController(navController)
            navController.addOnDestinationChangedListener { _, destination, _ ->
                if (destination.id == R.id.homeFragment || destination.id == R.id.myRewardFragment || destination.id == R.id.transactionFragment || destination.id == R.id.accountFragment) {
                    bottomNavigation.visibility = View.VISIBLE
                } else {
                    bottomNavigation.visibility = View.GONE
                }
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

    fun navigateFromTransactionToTransactionDetail(transactionCode: String) {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val action =
            TransactionFragmentDirections.actionTransactionFragmentToTransactionDetailFragment(
                transactionCode
            )
        navController.navigate(action)
    }

    fun navigateFromMyRewardToMyRewardDetail(transactionCode: String, type: Int) {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val action = when (type) {
            0 -> MyRewardFragmentDirections.actionMyRewardFragmentToMyRewardEGiftDetailFragment(
                transactionCode
            )
            1 -> MyRewardFragmentDirections.actionMyRewardFragmentToMyRewardPhysicalDetailFragment(
                transactionCode
            )
            else -> MyRewardFragmentDirections.actionMyRewardFragmentToMyRewardTopupDetailFragment(
                transactionCode
            )
        }
        navController.navigate(action)
    }

    fun navigateFromIMediaHistoryToMyRewardDetail(transactionCode: String) {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val action =
            IMediaHistoryFragmentDirections.actionIMediaHistoryFragmentToMyRewardTopupDetailFragment(
                transactionCode
            )
        navController.navigate(action)
    }

    fun exitSDK() {
        finish()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }

    companion object {
        lateinit var mainContext: Context
    }
}