package net.zackzhang.code.haze.common.view.ui.spring

import net.zackzhang.code.haze.base.util.dLog

private const val LOG = false

private const val TAG = "Spring"

internal fun log(msg: String) {
    if (!LOG) return
    dLog(msg, TAG)
}