package net.zackzhang.code.haze.base

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

abstract class BaseApp : Application() {

    companion object {

        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
            private set
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}