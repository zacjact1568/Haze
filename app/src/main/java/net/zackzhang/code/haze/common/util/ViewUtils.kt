package net.zackzhang.code.haze.common.util

import net.zackzhang.code.haze.common.Constants

object ViewUtils {

    fun Int?.toStringOrPlaceholder() = this?.toString() ?: Constants.PLACEHOLDER

    fun String?.orPlaceholder() = this ?: Constants.PLACEHOLDER
}