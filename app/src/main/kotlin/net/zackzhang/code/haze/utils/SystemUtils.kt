package net.zackzhang.code.haze.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.getSystemService
import net.zackzhang.code.util.checkNotNull

private val Context.inputMethodManager
    get() = checkNotNull(
        getSystemService<InputMethodManager>(),
        "`InputMethodManager` system service does not exist"
    )

fun View.showSoftInput() {
    requestFocus()
    context.inputMethodManager.showSoftInput(this, 0)
}

fun View.hideSoftInput() {
    context.inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}