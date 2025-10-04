package net.zackzhang.code.haze.common.view.ui.spring

import net.zackzhang.code.util.log as utilLog

private const val LOG = false

private const val TAG = "Spring"

internal fun log(msg: String) {
    if (!LOG) return
    utilLog(TAG, msg)
}