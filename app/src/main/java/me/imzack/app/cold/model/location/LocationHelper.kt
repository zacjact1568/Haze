package me.imzack.app.cold.model.location

import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import me.imzack.app.cold.App
import me.imzack.app.cold.common.Constant
import me.imzack.app.cold.util.LogUtil
import me.imzack.app.cold.util.SystemUtil

class LocationHelper {

    /** 异步获取高德定位数据 */
    fun getAMapLocationDataAsync(callback: (longitude: Double, latitude: Double) -> Unit) {
        val client = AMapLocationClient(App.context)
        client.setLocationOption(
                AMapLocationClientOption()
                        .setLocationMode(when (SystemUtil.locationMode) {
                            Constant.LOCATION_MODE_NONE -> TODO("未打开系统位置服务")
                            Constant.LOCATION_MODE_HIGH_ACCURACY -> AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
                            Constant.LOCATION_MODE_BATTERY_SAVING -> AMapLocationClientOption.AMapLocationMode.Battery_Saving
                            Constant.LOCATION_MODE_DEVICE_SENSORS -> AMapLocationClientOption.AMapLocationMode.Device_Sensors
                            else -> throw IllegalArgumentException("Wrong location mode")
                        })
                        .setOnceLocationLatest(true)
        )
        client.setLocationListener {
            when {
                it == null -> TODO("处理 AMapLocation 对象为空的情况")
                it.errorCode == 0 -> callback(it.longitude, it.latitude)
                else -> LogUtil.e("AMapError","location Error, ErrCode: ${it.errorCode}, errInfo: ${it.errorInfo}")
            }
            // 销毁定位客户端
            client.onDestroy()
        }
        client.startLocation()
    }

}
