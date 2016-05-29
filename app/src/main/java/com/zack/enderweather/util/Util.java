package com.zack.enderweather.util;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;

import com.zack.enderweather.R;
import com.zack.enderweather.application.EnderWeatherApp;
import com.zack.enderweather.bean.Weather;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Util {

    /** 检测网络是否可用 */
    public static boolean isNetworkAvailable() {
        Context context = EnderWeatherApp.getGlobalContext();
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return manager.getActiveNetworkInfo() != null && manager.getActiveNetworkInfo().isAvailable();
    }

    /** 将空气质量指数转换成对应的等级 */
    public static String parseAqi(String aqiStr) {
        int aqi = Integer.parseInt(aqiStr);
        Resources resources = EnderWeatherApp.getGlobalContext().getResources();
        if (aqi <= 50) {
            return resources.getString(R.string.aqi_level_1);
        } else if (aqi > 50 && aqi <= 100) {
            return resources.getString(R.string.aqi_level_2);
        } else if (aqi > 100 && aqi <= 150) {
            return resources.getString(R.string.aqi_level_3);
        } else if (aqi > 150 && aqi <= 200) {
            return resources.getString(R.string.aqi_level_4);
        } else if (aqi > 200 && aqi <= 300) {
            return resources.getString(R.string.aqi_level_5);
        } else if (aqi > 300) {
            return resources.getString(R.string.aqi_level_6);
        } else {
            return null;
        }
    }

    /** 产生表示星期的字符串数组 */
    public static String[] generateWeeks(String firstDate) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            calendar.setTime(sdf.parse(firstDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String[] weeks = new String[Weather.DAILY_FORECAST_LENGTH];
        Resources resources = EnderWeatherApp.getGlobalContext().getResources();
        for (int i = 0; i < weeks.length; i++) {
            if (i == 0) {
                weeks[i] = resources.getString(R.string.text_today);
            } else if (i == 1) {
                weeks[i] = resources.getString(R.string.text_tomorrow);
            } else {
                weeks[i] = parseWeek(calendar.get(Calendar.DAY_OF_WEEK));
            }
            //加1天
            calendar.add(Calendar.DATE, 1);
        }
        return weeks;
    }

    /** 将星期数转换成表示星期的字符串 */
    private static String parseWeek(int weekInt) {
        Resources resources = EnderWeatherApp.getGlobalContext().getResources();
        switch (weekInt) {
            case Calendar.MONDAY:
                return resources.getString(R.string.text_monday);
            case Calendar.TUESDAY:
                return resources.getString(R.string.text_tuesday);
            case Calendar.WEDNESDAY:
                return resources.getString(R.string.text_wednesday);
            case Calendar.THURSDAY:
                return resources.getString(R.string.text_thursday);
            case Calendar.FRIDAY:
                return resources.getString(R.string.text_friday);
            case Calendar.SATURDAY:
                return resources.getString(R.string.text_saturday);
            case Calendar.SUNDAY:
                return resources.getString(R.string.text_sunday);
            default:
                return null;
        }
    }
}
