package me.imzack.app.cold.util

import android.util.Log
import me.imzack.app.cold.model.DataManager

object LogUtil {

    private val DEF_TAG = "____"

    private val VERBOSE = 1
    private val DEBUG = 2
    private val INFO = 3
    private val WARN = 4
    private val ERROR = 5
    private val NOTHING = 6

    private var level = VERBOSE

    fun v(tag: String, msg: String) {
        if (level <= VERBOSE) {
            Log.v(tag, msg)
        }
    }

    fun d(tag: String, msg: String) {
        if (level <= DEBUG) {
            Log.d(tag, msg)
        }
    }

    fun d(tag: String, msg: Int) {
        if (level <= DEBUG) {
            Log.d(tag, msg.toString())
        }
    }

    fun i(tag: String, msg: String) {
        if (level <= INFO) {
            Log.i(tag, msg)
        }
    }

    fun w(tag: String, msg: String) {
        if (level <= WARN) {
            Log.w(tag, msg)
        }
    }

    fun e(tag: String, msg: String) {
        if (level <= ERROR) {
            Log.e(tag, msg)
        }
    }

    fun d(msg: String) {
        d(DEF_TAG, msg)
    }

    fun d(msg: Int) {
        d(DEF_TAG, msg)
    }

    fun here() {
        d("****HERE****")
    }

    fun logAllSharedPreferences() {
        d(DataManager.preferenceHelper.allValues.toString())
    }
}
