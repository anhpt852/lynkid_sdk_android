package vn.linkid.sdk.utils

import android.content.Context
import android.graphics.Rect
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import vn.linkid.sdk.databinding.ItemScrollerBinding
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.TimeZone


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
    return formatNumberWithDotSeparator(this.toLong())
}

fun Long.formatPrice(): String {
    return formatNumberWithDotSeparator(this)
}

fun Double.formatPrice(): String {
    return formatNumberWithDotSeparator(this)
}

private fun formatNumberWithDotSeparator(number: Number): String {
    val symbols = DecimalFormatSymbols(Locale.getDefault()).apply {
        groupingSeparator = '.'
        decimalSeparator = ','
    }
    val format = DecimalFormat("#,###.##", symbols).apply {
        groupingSize = 3
        isGroupingUsed = true
    }
    return format.format(number)
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
        if (expireDate != null) {
            outputFormat.format(expireDate)
        } else {
            ""
        }
    } catch (e: Exception) {
        ""
    }
}

fun formatDateToDayMonthYear(isoDateString: String): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(parseIsoDate(isoDateString))
}

fun formatDateTimeToHourMinuteDayMonthYear(isoDateString: String): String {
    val formatter = SimpleDateFormat("HH:mm - dd/MM/yyyy", Locale.getDefault())
    return formatter.format(parseIsoDate(isoDateString))
}

fun formatDateTimeToHourMinuteDayMonth(isoDateString: String): String {
    val formatter = SimpleDateFormat("HH:mm - dd/MM", Locale.getDefault())
    return formatter.format(parseIsoDate(isoDateString))
}

fun parseIsoDate(isoDateString: String): Date {
    val isoFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    isoFormatter.timeZone = TimeZone.getTimeZone("UTC")  // Set timezone to UTC to match the 'Z' in the ISO string
    return isoFormatter.parse(isoDateString) ?: Date()  // Return current date if parsing fails
}

fun View.adjustWhenKeyboardShown(bottomButton: View) {
    viewTreeObserver.addOnGlobalLayoutListener {
        val rect = Rect()
        getWindowVisibleDisplayFrame(rect)
        val screenHeight = rootView.height
        val keypadHeight = screenHeight - rect.bottom

        if (keypadHeight > screenHeight * 0.15) {
            // Keyboard is shown
            bottomButton.translationY = -keypadHeight.toFloat()
        } else {
            // Keyboard is hidden
            bottomButton.translationY = 0f
        }
    }
}
