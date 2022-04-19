package net.zackzhang.code.haze.base.util

operator fun <T> MutableList<T>.plusAssign(list: List<T>?) {
    list?.let { addAll(it) }
}

inline fun <T> Iterable<T>.forEachCounted(count: Int, action: (T) -> Unit) {
    forEachIndexed { index, item ->
        if (index >= count) return@forEachIndexed
        action(item)
    }
}