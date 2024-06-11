package vn.linkid.sdk.utils

import android.text.Selection
import android.text.Spannable
import android.text.method.MovementMethod
import android.view.KeyEvent
import android.view.MotionEvent
import android.widget.TextView


internal class DefaultMovementMethod private constructor() : MovementMethod {
    override fun initialize(widget: TextView, text: Spannable) {
        Selection.setSelection(text, 0)
    }

    override fun onKeyDown(
        widget: TextView,
        text: Spannable,
        keyCode: Int,
        event: KeyEvent
    ): Boolean = false

    override fun onKeyUp(
        widget: TextView,
        text: Spannable,
        keyCode: Int,
        event: KeyEvent
    ): Boolean = false

    override fun onKeyOther(view: TextView, text: Spannable, event: KeyEvent): Boolean = false

    override fun onTakeFocus(widget: TextView, text: Spannable, direction: Int) = Unit

    override fun onTrackballEvent(widget: TextView, text: Spannable, event: MotionEvent): Boolean =
        false

    override fun onTouchEvent(widget: TextView, text: Spannable, event: MotionEvent): Boolean =
        false

    override fun onGenericMotionEvent(
        widget: TextView,
        text: Spannable,
        event: MotionEvent
    ): Boolean = false

    override fun canSelectArbitrarily(): Boolean = false

    companion object {
        private var sInstance: DefaultMovementMethod? = null

        val instance: MovementMethod
            get() {
                if (sInstance == null) {
                    sInstance = DefaultMovementMethod()
                }

                return sInstance!!
            }
    }
}