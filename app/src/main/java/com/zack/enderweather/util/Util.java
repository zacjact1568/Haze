package com.zack.enderweather.util;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;

import com.zack.enderweather.R;
import com.zack.enderweather.application.EnderWeatherApp;
import com.zack.enderweather.bean.HeWeather;
import com.zack.enderweather.bean.Weather;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Util {

    private static final String LOG_TAG = "Util";

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

    /** 将和风天气bean中的内容解析到天气bean中 */
    public static void parseHeWeatherData(HeWeather heWeather, Weather weather) {
        HeWeather.HeWeatherAPI api = heWeather.getHeWeatherAPIList().get(0);

        HeWeather.HeWeatherAPI.Basic basic = api.getBasic();
        weather.getBasicInfo().setExtraValues(
                basic.getCity(),
                basic.getUpdate().getLoc()
        );

        HeWeather.HeWeatherAPI.Now now = api.getNow();
        weather.getCurrentInfo().setExtraValues(
                now.getCond().getTxt(),
                now.getTmp(),
                now.getFl(),
                now.getHum(),
                now.getPcpn(),
                now.getPres(),
                now.getVis(),
                now.getWind().getSpd(),
                now.getWind().getSc(),
                now.getWind().getDeg(),
                now.getWind().getDir()
        );

        int startIndex = Weather.HOURLY_FORECAST_LENGTH - api.getHourlyForecastList().size();
        for (int i = 0; i < Weather.HOURLY_FORECAST_LENGTH; i++) {
            if (i < startIndex) {
                weather.getHourlyForecastList().get(i).setEmptyValues();
            } else {
                HeWeather.HeWeatherAPI.HourlyForecast hourlyForecast = api.getHourlyForecastList().get(i - startIndex);
                weather.getHourlyForecastList().get(i).setExtraValues(
                        hourlyForecast.getDate(),
                        hourlyForecast.getTmp(),
                        hourlyForecast.getWind().getSpd(),
                        hourlyForecast.getWind().getSc(),
                        hourlyForecast.getWind().getDeg(),
                        hourlyForecast.getWind().getDir(),
                        hourlyForecast.getPop(),
                        hourlyForecast.getHum(),
                        hourlyForecast.getPres()
                );
            }
        }

        for (int i = 0; i < Weather.DAILY_FORECAST_LENGTH; i++) {
            HeWeather.HeWeatherAPI.DailyForecast dailyForecast = api.getDailyForecastList().get(i);
            weather.getDailyForecastList().get(i).setExtraValues(
                    dailyForecast.getDate(),
                    dailyForecast.getAstro().getSr(),
                    dailyForecast.getAstro().getSs(),
                    dailyForecast.getTmp().getMax(),
                    dailyForecast.getTmp().getMin(),
                    dailyForecast.getWind().getSpd(),
                    dailyForecast.getWind().getSc(),
                    dailyForecast.getWind().getDeg(),
                    dailyForecast.getWind().getDir(),
                    dailyForecast.getCond().getTxtD(),
                    dailyForecast.getCond().getTxtN(),
                    dailyForecast.getPcpn(),
                    dailyForecast.getPop(),
                    dailyForecast.getHum(),
                    dailyForecast.getPres(),
                    dailyForecast.getVis()
            );
        }

        if (api.getAqi() != null) {
            HeWeather.HeWeatherAPI.Aqi.City cityAqi = api.getAqi().getCity();
            if (cityAqi.getCo() == null) {
                //部分城市只有三个数据
                LogUtil.i(LOG_TAG, "AQI数据不全");
                weather.getAirQuality().setPartValues(
                        cityAqi.getAqi(),
                        cityAqi.getPm10(),
                        cityAqi.getPm25()
                );
            } else {
                weather.getAirQuality().setExtraValues(
                        cityAqi.getAqi(),
                        cityAqi.getCo(),
                        cityAqi.getNo2(),
                        cityAqi.getO3(),
                        cityAqi.getPm10(),
                        cityAqi.getPm25(),
                        cityAqi.getQlty(),
                        cityAqi.getSo2()
                );
            }
        } else {
            //部分城市没有AQI数据
            LogUtil.i(LOG_TAG, "没有AQI数据");
            weather.getAirQuality().setEmptyValues();
        }

        if (api.getSuggestion() != null) {
            HeWeather.HeWeatherAPI.Suggestion suggestion = api.getSuggestion();
            weather.getLifeSuggestion().setExtraValues(
                    suggestion.getComf().getBrf(),
                    suggestion.getDrsg().getBrf(),
                    suggestion.getUv().getBrf(),
                    suggestion.getCw().getBrf(),
                    suggestion.getTrav().getBrf(),
                    suggestion.getFlu().getBrf(),
                    suggestion.getSport().getBrf()
            );
        } else {
            LogUtil.i(LOG_TAG, "没有Suggestion数据");
            weather.getLifeSuggestion().setEmptyValues();
        }
    }
}
