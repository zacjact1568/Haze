package net.zackzhang.code.haze.common.util

object NumberUtils {

    fun parseIntRange(intRange: String?): IntRange? {
        intRange ?: return null
        val ends = intRange.split('-')
        val first = ends[0].toInt()
        return when (ends.size) {
            1 -> first..first
            2 -> first..ends[1].toInt()
            else -> null
        }
    }

    fun presentIntRange(intRange: IntRange?): String? {
        intRange ?: return null
        val first = intRange.first
        val last = intRange.last
        return if (first == last) first.toString() else "$first-$last"
    }
}