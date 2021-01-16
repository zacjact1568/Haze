package net.zackzhang.code.haze.view.contract

import androidx.annotation.StringRes

interface BaseViewContract {

    fun showToast(@StringRes msgResId: Int)

    fun exit()
}