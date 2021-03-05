package net.zackzhang.code.haze.common.util

import androidx.annotation.RawRes
import net.zackzhang.code.haze.HazeApplication as App
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object ResourceUtils {

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
}