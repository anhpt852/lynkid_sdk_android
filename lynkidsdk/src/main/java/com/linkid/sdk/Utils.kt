package com.linkid.sdk

import android.content.Context
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


fun getStatusBarHeight(view: View): Int = ViewCompat.getRootWindowInsets(view)?.getInsets(
    WindowInsetsCompat.Type.systemBars()
)?.top ?: 0

fun getNavigationBarHeight(view: View): Int = ViewCompat.getRootWindowInsets(view)?.getInsets(
    WindowInsetsCompat.Type.systemBars()
)?.bottom ?: 0



fun Context.dpToPx(dp: Int): Int {
    val density = resources.displayMetrics.density
    return (dp * density).toInt()
}
