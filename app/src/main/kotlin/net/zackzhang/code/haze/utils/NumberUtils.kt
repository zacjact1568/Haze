package net.zackzhang.code.haze.utils

import kotlin.math.max
import kotlin.math.min

data class EndCoordinates2D(val startX: Int, val startY: Int, val endX: Int, val endY: Int)

data class Coordinate2D(
    var x: Int = 0,
    var y: Int = 0,
    private val column: Int,
) {

    operator fun plusAssign(distance: Int) {
        val destination = x + distance
        if (destination < column) {
            x = destination
        } else {
            x = destination - column
            y++
        }
    }
}

fun ByteArray.toHexString() = joinToString("") { "%02x".format(it) }

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

fun IntRange.ensureNotEmpty() = if (isEmpty()) null else this

fun IntRange.intersection(other: IntRange) =
    when {
        // 没有交集
        isEmpty() || other.isEmpty() || first > other.last || last < other.first -> null
        // 包含
        first <= other.first && last >= other.last -> other
        first >= other.first && last <= other.last -> this
        // 有交集
        else -> max(first, other.first)..min(last, other.last)
    }