package net.zackzhang.code.haze

import android.app.Application
import android.content.Context
import net.zackzhang.code.util.provideContext

class App : Application() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        provideContext(base)
    }
}