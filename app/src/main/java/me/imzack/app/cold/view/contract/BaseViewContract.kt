package me.imzack.app.cold.view.contract

import android.support.annotation.StringRes

interface BaseViewContract {

    fun showToast(@StringRes msgResId: Int)

    fun exit()
}