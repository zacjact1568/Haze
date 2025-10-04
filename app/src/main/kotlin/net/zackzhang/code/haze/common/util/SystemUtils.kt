package net.zackzhang.code.haze.common.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

private val Context.inputMethodManager
    get() = getSystemService(InputMethodManager::class.java)!!

fun View.showSoftInput() {
    requestFocus()
    context.inputMethodManager.showSoftInput(this, 0)
}

fun View.hideSoftInput() {
    context.inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}