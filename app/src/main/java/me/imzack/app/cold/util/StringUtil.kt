package me.imzack.app.cold.util

import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan

object StringUtil {

    fun addColorSpan(cs: CharSequence, color: Int): SpannableString {
        val ss = SpannableString(cs)
        ss.setSpan(ForegroundColorSpan(color), 0, cs.length, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
        return ss
    }

    fun addWhiteColorSpan(cs: CharSequence) = addColorSpan(cs, Color.WHITE)
}