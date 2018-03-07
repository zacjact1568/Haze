package me.imzack.app.cold.model.location

import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import me.imzack.app.cold.App

class LocationHelper {

    companion object {

        val LOC_MODE_BATTERY_SAVING = -1
        val LOC_MODE_DEVICE_SENSORS = 0
        val LOC_MODE_HIGH_ACCURACY = 1
    }

    /** 异步获取高德定位数据  */
    fun getAMapLocationDataAsync(locationMode: Int) {
        val aMapLocationClient = AMapLocationClient(App.context)
        aMapLocationClient.setLocationOption(AMapLocationClientOption()
                .setLocationMode(getAMapLocationMode(locationMode))
                //TODO 设置定位参数
        )
        aMapLocationClient.startLocation()
        aMapLocationClient.setLocationListener(AMapLocationListener { aMapLocation ->
            if (aMapLocation == null) {
                return@AMapLocationListener
            }
            //TODO ...
        })
    }

    private fun getAMapLocationMode(locationMode: Int) =
            when (locationMode) {
                LOC_MODE_BATTERY_SAVING -> AMapLocationClientOption.AMapLocationMode.Battery_Saving
                LOC_MODE_DEVICE_SENSORS -> AMapLocationClientOption.AMapLocationMode.Device_Sensors
                LOC_MODE_HIGH_ACCURACY -> AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
                else -> throw RuntimeException("Invalid location mode")
            }
}
