package net.zackzhang.app.cold

import android.app.Application
import android.content.Context
import android.preference.PreferenceManager
import android.support.annotation.RawRes
import com.facebook.stetho.Stetho
import net.zackzhang.app.cold.common.Constant
import net.zackzhang.app.cold.event.EventBusIndex
import net.zackzhang.app.cold.model.DataManager
import org.greenrobot.eventbus.EventBus
import java.io.FileOutputStream
import java.io.IOException

class App : Application() {

    companion object {

        lateinit var context: Context
            private set

        val eventBus
            get() = EventBus.getDefault()!!
    }

    override fun onCreate() {
        super.onCreate()

        context = applicationContext

        initStetho()

        initEventBus()

        initPreferences()

        initDatabases()

        initData()
    }

    private fun initStetho() {
        Stetho.initializeWithDefaults(this)
    }

    private fun initEventBus() {
        EventBus.builder().addIndex(EventBusIndex()).installDefaultEventBus()
    }

    private fun initPreferences() {
        // 设定 preferences 默认值
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)
    }

    /** 初始化数据库 */
    private fun initDatabases() {
        copyDatabase(Constant.HE_WEATHER_CONDITION_DB_FN, R.raw.db_he_weather_condition)
        copyDatabase(Constant.HE_WEATHER_LOCATION_DB_FN, R.raw.db_he_weather_location)
    }

    /** 复制数据库 */
    private fun copyDatabase(filename: String, @RawRes resId: Int) {
        val file = getDatabasePath(filename)
        if (file.exists()) return
        val dir = file.parentFile
        if (dir.exists() || dir.mkdir()) {
            // 若目录不存在，exists 返回 false，就去创建目录，mkdir 返回 true，说明创建成功
            // 若目录已存在，exists 返回 true，就不会执行 mkdir 了
            // 若目录不存在，mkdir 也返回 false，不会执行下面的内容
            try {
                val iStream = resources.openRawResource(resId)
                //FileOutputStream也有创建文件的功能
                val foStream = FileOutputStream(file)
                val buffer = ByteArray(400000)
                var count: Int
                while (true) {
                    count = iStream.read(buffer)
                    if (count <= 0) break
                    foStream.write(buffer, 0, count)
                }
                foStream.close()
                iStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun initData() {
        DataManager.loadData()
    }
}
