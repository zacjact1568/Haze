package com.zack.enderweather.location;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.zack.enderweather.application.EnderWeatherApp;

public class LocationHelper {

    public static final int LOC_MODE_BATTERY_SAVING = -1;
    public static final int LOC_MODE_DEVICE_SENSORS = 0;
    public static final int LOC_MODE_HIGH_ACCURACY = 1;

    /** 异步获取高德定位数据 */
    public void getAMapLocationDataAsync(int locationMode) {
        AMapLocationClient aMapLocationClient = new AMapLocationClient(EnderWeatherApp.getGlobalContext());
        aMapLocationClient.setLocationOption(new AMapLocationClientOption()
                .setLocationMode(getAMapLocationMode(locationMode))
                //TODO 设置定位参数
        );
        aMapLocationClient.startLocation();
        aMapLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation == null) {
                    return;
                }
                //TODO ...
            }
        });
    }

    private AMapLocationClientOption.AMapLocationMode getAMapLocationMode(int locationMode) {
        switch (locationMode) {
            case LOC_MODE_BATTERY_SAVING:
                return AMapLocationClientOption.AMapLocationMode.Battery_Saving;
            case LOC_MODE_DEVICE_SENSORS:
                return AMapLocationClientOption.AMapLocationMode.Device_Sensors;
            case LOC_MODE_HIGH_ACCURACY:
                return AMapLocationClientOption.AMapLocationMode.Hight_Accuracy;
            default:
                throw new RuntimeException("Invalid location mode");
        }
    }

    public interface PermissionDelegate {

        void showPreviouslyRequestPermissionsDialog();

        void onRequestPermissions();

        void showAddCityRequestDialog();
    }
}
