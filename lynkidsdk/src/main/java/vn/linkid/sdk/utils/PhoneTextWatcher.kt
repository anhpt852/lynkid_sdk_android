package vn.linkid.sdk.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class PhoneTextWatcher(
    private val editText: TextInputEditText,
    private val textInputLayout: TextInputLayout,
    private val button: Button,
    private val errorMessage: String = "Please enter a valid Vietnamese phone number"
) : TextWatcher {
    private var isFormatting = false
    private var deletingHyphen = false
    private var hyphenStart = 0
    private val maxLength = 12

    init {
        updateButtonOpacity(false)
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        if (!isFormatting) {
            deletingHyphen = count == 1 && s[start] == ' '
            hyphenStart = start
        }
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        // Not used
    }

    override fun afterTextChanged(s: Editable) {
        if (!isFormatting) {
            isFormatting = true
            val cursorPosition = editText.selectionStart
            val formatted = formatNumber(s.toString())
            s.replace(0, s.length, formatted)

            var cursorOffset = 0
            if (deletingHyphen) {
                cursorOffset = -1
            } else if (formatted.length > s.length) {
                cursorOffset = 1
            }

            val newCursorPosition = cursorPosition + cursorOffset
            editText.setSelection(newCursorPosition.coerceIn(0, formatted.length))

            isFormatting = false

            val isValid = isValid()
            updateButtonOpacity(isValid)
            updateErrorStatus(isValid)
        }
    }

    private fun formatNumber(number: String): String {
        val digitsOnly = number.replace(Regex("\\D"), "")
        val formatted = StringBuilder()

        if (digitsOnly.startsWith("84")) {
            formatted.append("+84 ")
            var index = 2
            while (index < digitsOnly.length && formatted.length < maxLength) {
                if ((index - 2) % 3 == 0 && index != 2) {
                    formatted.append(" ")
                }
                formatted.append(digitsOnly[index])
                index++
            }
        } else {
            var index = 0
            while (index < digitsOnly.length && formatted.length < maxLength) {
                if (index % 3 == 0 && index != 0) {
                    formatted.append(" ")
                }
                formatted.append(digitsOnly[index])
                index++
            }
        }

        return formatted.toString()
    }

    fun isValid(): Boolean {
        val number = editText.text.toString().replace(Regex("\\D"), "")
        return (number.length == 10 && number.startsWith("0")) ||
                (number.length == 11 && number.startsWith("84"))
    }

    private fun updateButtonOpacity(isValid: Boolean) {
        button.alpha = if (isValid) 1.0f else 0.6f
    }

    private fun updateErrorStatus(isValid: Boolean) {
        if (isValid) {
            textInputLayout.error = null
        } else {
            textInputLayout.error = errorMessage
        }
    }
}