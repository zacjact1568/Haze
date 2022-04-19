package net.zackzhang.code.haze.base.util

import android.util.Log

private const val VERBOSE = 1
private const val DEBUG = 2
private const val INFO = 3
private const val WARN = 4
private const val ERROR = 5
private const val NOTHING = 6

var logLevel = VERBOSE

private val defaultTag: String
    get() {
        val element = Thread.currentThread().stackTrace[4]
        return "${element.className.substringAfterLast('.')}/${element.methodName}/${element.lineNumber}"
    }

fun vLog(msg: String, tag: String = defaultTag) {
    if (logLevel <= VERBOSE) {
        Log.v(tag, msg)
    }
}

fun dLog(msg: String, tag: String = defaultTag) {
    if (logLevel <= DEBUG) {
        Log.d(tag, msg)
    }
}

fun iLog(msg: String, tag: String = defaultTag) {
    if (logLevel <= INFO) {
        Log.i(tag, msg)
    }
}

fun wLog(msg: String, tag: String = defaultTag) {
    if (logLevel > WARN) return
    Log.w(tag, msg)
}

fun wLog(tr: Throwable) {
    if (logLevel > WARN) return
    val name = tr::class.qualifiedName
    val msg = tr.localizedMessage
    when {
        name != null && msg != null -> wLog("$name: $msg")
        name != null -> wLog(name)
        msg != null -> wLog(msg)
        else -> wLog("**** WARNING ****")
    }
}

fun eLog(msg: String, tag: String = defaultTag) {
    if (logLevel <= ERROR) {
        Log.e(tag, msg)
    }
}

fun dLog(msg: Int, tag: String = defaultTag) {
    dLog(msg.toString(), tag)
}

fun logHere() {
    dLog("**** HERE ****", defaultTag)
}