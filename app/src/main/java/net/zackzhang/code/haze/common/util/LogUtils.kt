package net.zackzhang.code.haze.common.util

import android.util.Log

object LogUtils {

    private const val VERBOSE = 1
    private const val DEBUG = 2
    private const val INFO = 3
    private const val WARN = 4
    private const val ERROR = 5
    private const val NOTHING = 6

    var level = VERBOSE

    private val defaultTag: String
        get() {
            val element = Thread.currentThread().stackTrace[4]
            return "${element.className.substringAfterLast('.')}/${element.methodName}/${element.lineNumber}"
        }

    fun v(msg: String, tag: String = defaultTag) {
        if (level <= VERBOSE) {
            Log.v(tag, msg)
        }
    }

    fun d(msg: String, tag: String = defaultTag) {
        if (level <= DEBUG) {
            Log.d(tag, msg)
        }
    }

    fun i(msg: String, tag: String = defaultTag) {
        if (level <= INFO) {
            Log.i(tag, msg)
        }
    }

    fun w(msg: String, tag: String = defaultTag) {
        if (level <= WARN) {
            Log.w(tag, msg)
        }
    }

    fun e(msg: String, tag: String = defaultTag) {
        if (level <= ERROR) {
            Log.e(tag, msg)
        }
    }

    fun d(msg: Int, tag: String = defaultTag) {
        d(msg.toString(), tag)
    }

    fun here() {
        d("**** HERE ****", defaultTag)
    }
}
