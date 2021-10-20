package net.zackzhang.code.haze.common.util

operator fun <T> MutableList<T>.plusAssign(list: List<T>?) {
    list?.let { addAll(it) }
}