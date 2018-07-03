package net.zackzhang.app.cold.util

object AMapLocationUtil {

    /** 将错误码转换为当前语言的描述信息 */
    fun parseErrorCode(code: Int): Int {
        val codeStr = if (code == -1) "minus_1" else code.toString()
        val resId = ResourceUtil.getStringResourceId("error_amap_location_error_code_$codeStr")
        if (resId == 0) throw IllegalArgumentException("Unknown code")
        return resId
    }
}