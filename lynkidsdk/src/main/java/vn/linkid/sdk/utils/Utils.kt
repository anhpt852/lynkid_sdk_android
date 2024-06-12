package vn.linkid.sdk.utils

import android.content.Context
import android.icu.text.NumberFormat
import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import android.util.Log
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import vn.linkid.sdk.databinding.ItemScrollerBinding
import java.util.Date
import java.util.Locale


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

fun Int.formatPrice(): String {
    val format = NumberFormat.getNumberInstance(Locale.getDefault())
    return format.format(this)
}

fun Long.formatPrice(): String {
    val format = NumberFormat.getNumberInstance(Locale.getDefault())
    return format.format(this)
}

fun Double.formatPrice(): String {
    val format = NumberFormat.getNumberInstance(Locale.getDefault())
    return format.format(this)
}

fun RecyclerView.handleScroll(itemScrollerBinding: ItemScrollerBinding) {
    post {
        val parentWidth = itemScrollerBinding.parent.width
        val layoutParams = itemScrollerBinding.progress.layoutParams
        val initialX = itemScrollerBinding.progress.x
        layoutManager?.let {
            val totalWidth = this.computeHorizontalScrollRange()
            val visibleWidth = this.computeHorizontalScrollExtent()
            val percent =
                visibleWidth.toFloat() / totalWidth.toFloat()
            layoutParams.width = (parentWidth * percent).toInt()
            itemScrollerBinding.progress.layoutParams = layoutParams
            addOnScrollListener(object :
                RecyclerView.OnScrollListener() {

                override fun onScrolled(
                    recyclerView: RecyclerView,
                    dx: Int,
                    dy: Int,
                ) {
                    Log.d("TAG", "onScrolled: $dx, $dy")
                    super.onScrolled(recyclerView, dx, dy)
                    val scrolledWidth =
                        this@handleScroll.computeHorizontalScrollOffset()
                    val updatedVisibleWidthRecyclerView =
                        visibleWidth + scrolledWidth
                    val scrolledWidthScroller =
                        ((parentWidth.toFloat() / totalWidth) * scrolledWidth)
                    itemScrollerBinding.progress.x =
                        initialX + scrolledWidthScroller
                }
            })
        }
    }
}

fun generateCacheKey(url: String, params: Map<String, Any>): String {
    return url + params.entries.sortedBy { it.key }.joinToString(separator = "&", prefix = "?") {
        "${it.key}=${it.value}"
    }
}

fun formatDate(date: String?): String {
    if (date.isNullOrEmpty()) return ""
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        val expireDate = inputFormat.parse(date)?.let {
            Date(it.time + 7 * 3600 * 1000) // Adjusting for UTC+7
        }

        val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        outputFormat.format(expireDate)
    } catch (e: Exception) {
        ""
    }
}
