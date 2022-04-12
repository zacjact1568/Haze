package net.zackzhang.code.haze.common.util

import android.content.Context
import androidx.annotation.*
import net.zackzhang.code.haze.R
import net.zackzhang.code.haze.App
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

val appName = getString(R.string.app_name).lowercase()

val supportShorterExpression: Boolean get() {
    val locales = App.context.resources.configuration.locales
    repeat(locales.size()) {
        when (locales.get(it)) {
            Locale.SIMPLIFIED_CHINESE -> return true
            Locale.ENGLISH -> return false
        }
    }
    return false
}

fun copyRawFile(@RawRes resId: Int, path: File) {
    if (path.exists()) return
    val dir = path.parentFile
    if (dir != null && (dir.exists() || dir.mkdir())) {
        // 若目录不存在，exists 返回 false，就去创建目录，mkdir 返回 true，说明创建成功
        // 若目录已存在，exists 返回 true，就不会执行 mkdir 了
        // 若目录不存在，mkdir 也返回 false，不会执行下面的内容
        try {
            val iStream = App.context.resources.openRawResource(resId)
            // FileOutputStream 也有创建文件的功能
            val foStream = FileOutputStream(path)
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

fun Context.getDimension(@DimenRes id: Int) = resources.getDimensionPixelSize(id)

fun Context.getInteger(@IntegerRes id: Int) = resources.getInteger(id)

fun Context.getFormattedQuantityString(@PluralsRes id: Int, quantity: Int) =
    resources.getQuantityString(id, quantity, quantity)

fun getString(@StringRes id: Int) = App.context.getString(id)

// 用 vararg 格式化后，String 变成了类 + 地址，不知道为啥
fun getString(@StringRes id: Int, arg: String) = App.context.getString(id, arg)

fun getAppColorRes(@ColorRes id: Int) = App.context.getColor(id)