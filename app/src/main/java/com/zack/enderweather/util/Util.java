package com.zack.enderweather.util;

import android.content.Context;
import android.net.ConnectivityManager;

import com.zack.enderweather.application.EnderWeatherApp;

public class Util {

    /** 检测网络是否可用 */
    public static boolean isNetworkAvailable() {
        Context context = EnderWeatherApp.getGlobalContext();
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return manager.getActiveNetworkInfo() != null && manager.getActiveNetworkInfo().isAvailable();
    }
}
