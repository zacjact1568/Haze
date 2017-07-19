package me.imzack.app.cold.model.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import me.imzack.app.cold.application.EnderWeatherApp;
import me.imzack.app.cold.model.bean.City;
import me.imzack.app.cold.model.bean.Weather;
import me.imzack.app.cold.model.bean.AirQuality;
import me.imzack.app.cold.model.bean.BasicInfo;
import me.imzack.app.cold.model.bean.CurrentInfo;
import me.imzack.app.cold.model.bean.DailyForecast;
import me.imzack.app.cold.model.bean.HourlyForecast;
import me.imzack.app.cold.model.bean.LifeSuggestion;
import me.imzack.app.cold.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class DatabaseDispatcher {

    private static final String LOG_TAG = "DatabaseDispatcher";

    public static final String DB_NAME = "com.zack.enderweather.db";
    public static final String DB_CITY_CN = "city_cn.db";

    public static final int DB_VERSION = 1;

    private static final String SQL_QUERY_CITY = "select city_id, city_zh, pref_zh, prov_zh from city where (city_zh like ?) or (city_en like ?)";

    private SQLiteDatabase database, cityDB;

    private static DatabaseDispatcher ourInstance = new DatabaseDispatcher();

    private DatabaseDispatcher() {
        Context context = EnderWeatherApp.getGlobalContext();

        DatabaseOpenHelper dbHelper = new DatabaseOpenHelper(context, DB_NAME, null, DB_VERSION);
        database = dbHelper.getWritableDatabase();

        cityDB = SQLiteDatabase.openDatabase(context.getDatabasePath(DB_CITY_CN).getPath(), null, SQLiteDatabase.OPEN_READONLY);
    }

    public static DatabaseDispatcher getInstance() {
        return ourInstance;
    }

    //*****************City*****************

    public void queryCityLike(String input, List<City> resultList) {
        Cursor cursor = cityDB.rawQuery(SQL_QUERY_CITY, new String[]{input + "%", input + "%"});
        if (cursor.moveToFirst()) {
            do {
                resultList.add(new City(
                        cursor.getString(cursor.getColumnIndex("city_id")),
                        cursor.getString(cursor.getColumnIndex("city_zh")),
                        cursor.getString(cursor.getColumnIndex("pref_zh")),
                        cursor.getString(cursor.getColumnIndex("prov_zh"))
                ));
            } while (cursor.moveToNext());
        } else {
            LogUtil.i(LOG_TAG, "未查询到数据");
        }
        cursor.close();
    }

    //*****************Weather*****************

    public void saveWeather(Weather weather) {
        if (weather != null) {
            ContentValues values = new ContentValues();
            if (weather.getBasicInfo() != null) {
                convert(weather.getBasicInfo(), values);
                database.insert("basic_info", null, values);
                values.clear();
            }
            if (weather.getCurrentInfo() != null) {
                convert(weather.getCurrentInfo(), values);
                database.insert("current_info", null, values);
                values.clear();
            }
            if (weather.getHourlyForecastList() != null) {
                for (HourlyForecast hf : weather.getHourlyForecastList()) {
                    convert(hf, values);
                    database.insert("hourly_forecast", null, values);
                    values.clear();
                }
            }
            if (weather.getDailyForecastList() != null) {
                for (DailyForecast df : weather.getDailyForecastList()) {
                    convert(df, values);
                    database.insert("daily_forecast", null, values);
                    values.clear();
                }
            }
            if (weather.getAirQuality() != null) {
                convert(weather.getAirQuality(), values);
                database.insert("air_quality", null, values);
                values.clear();
            }
            if (weather.getLifeSuggestion() != null) {
                convert(weather.getLifeSuggestion(), values);
                database.insert("life_suggestion", null, values);
                values.clear();
            }
        }
    }

    public List<Weather> loadWeather() {
        List<Weather> weatherList = new ArrayList<>();

        Cursor biCursor = database.rawQuery("select * from basic_info", null);
        Cursor ciCursor = database.rawQuery("select * from current_info", null);
        Cursor hfCursor = database.rawQuery("select * from hourly_forecast", null);
        Cursor dfCursor = database.rawQuery("select * from daily_forecast", null);
        Cursor aqCursor = database.rawQuery("select * from air_quality", null);
        Cursor lsCursor = database.rawQuery("select * from life_suggestion", null);

        if (biCursor.moveToFirst()) {
            do {
                Weather weather = new Weather(
                        biCursor.getString(biCursor.getColumnIndex("city_id")),
                        biCursor.getString(biCursor.getColumnIndex("city_name"))
                );
                weather.getBasicInfo().setExtraValues(
                        biCursor.getString(biCursor.getColumnIndex("update_time"))
                );
                weatherList.add(weather);
            } while (biCursor.moveToNext());
        }
        biCursor.close();

        if (ciCursor.moveToFirst()) {
            for (Weather weather : weatherList) {
                weather.getCurrentInfo().setExtraValues(
                        ciCursor.getString(ciCursor.getColumnIndex("condition")),
                        ciCursor.getString(ciCursor.getColumnIndex("temperature")),
                        ciCursor.getString(ciCursor.getColumnIndex("sensible_temp")),
                        ciCursor.getString(ciCursor.getColumnIndex("humidity")),
                        ciCursor.getString(ciCursor.getColumnIndex("precipitation")),
                        ciCursor.getString(ciCursor.getColumnIndex("pressure")),
                        ciCursor.getString(ciCursor.getColumnIndex("visibility")),
                        ciCursor.getString(ciCursor.getColumnIndex("wind_speed")),
                        ciCursor.getString(ciCursor.getColumnIndex("wind_scale")),
                        ciCursor.getString(ciCursor.getColumnIndex("wind_deg")),
                        ciCursor.getString(ciCursor.getColumnIndex("wind_direction"))
                );
                if (!ciCursor.moveToNext()) {
                    break;
                }
            }
        }
        ciCursor.close();

        if (hfCursor.moveToFirst()) {
            for (Weather weather : weatherList) {
                for (int i = 0; i < Weather.HOURLY_FORECAST_LENGTH; i++) {
                    weather.getHourlyForecastList().get(i).setExtraValues(
                            hfCursor.getString(hfCursor.getColumnIndex("time")),
                            hfCursor.getString(hfCursor.getColumnIndex("temperature")),
                            hfCursor.getString(hfCursor.getColumnIndex("wind_speed")),
                            hfCursor.getString(hfCursor.getColumnIndex("wind_scale")),
                            hfCursor.getString(hfCursor.getColumnIndex("wind_deg")),
                            hfCursor.getString(hfCursor.getColumnIndex("wind_direction")),
                            hfCursor.getString(hfCursor.getColumnIndex("pcpn_prob")),
                            hfCursor.getString(hfCursor.getColumnIndex("humidity")),
                            hfCursor.getString(hfCursor.getColumnIndex("pressure"))
                    );
                    if (!hfCursor.moveToNext()) {
                        break;
                    }
                }
            }
        }
        hfCursor.close();

        if (dfCursor.moveToFirst()) {
            for (Weather weather : weatherList) {
                for (int i = 0; i < Weather.DAILY_FORECAST_LENGTH; i++) {
                    weather.getDailyForecastList().get(i).setExtraValues(
                            dfCursor.getString(dfCursor.getColumnIndex("date")),
                            dfCursor.getString(dfCursor.getColumnIndex("sunrise_time")),
                            dfCursor.getString(dfCursor.getColumnIndex("sunset_time")),
                            dfCursor.getString(dfCursor.getColumnIndex("max_temp")),
                            dfCursor.getString(dfCursor.getColumnIndex("min_temp")),
                            dfCursor.getString(dfCursor.getColumnIndex("wind_speed")),
                            dfCursor.getString(dfCursor.getColumnIndex("wind_scale")),
                            dfCursor.getString(dfCursor.getColumnIndex("wind_deg")),
                            dfCursor.getString(dfCursor.getColumnIndex("wind_direction")),
                            dfCursor.getString(dfCursor.getColumnIndex("condition_day")),
                            dfCursor.getString(dfCursor.getColumnIndex("condition_night")),
                            dfCursor.getString(dfCursor.getColumnIndex("precipitation")),
                            dfCursor.getString(dfCursor.getColumnIndex("pcpn_prob")),
                            dfCursor.getString(dfCursor.getColumnIndex("humidity")),
                            dfCursor.getString(dfCursor.getColumnIndex("pressure")),
                            dfCursor.getString(dfCursor.getColumnIndex("visibility"))
                    );
                    if (!dfCursor.moveToNext()) {
                        break;
                    }
                }
            }
        }
        dfCursor.close();

        if (aqCursor.moveToFirst()) {
            for (Weather weather : weatherList) {
                weather.getAirQuality().setExtraValues(
                        aqCursor.getString(aqCursor.getColumnIndex("aqi")),
                        aqCursor.getString(aqCursor.getColumnIndex("co")),
                        aqCursor.getString(aqCursor.getColumnIndex("no2")),
                        aqCursor.getString(aqCursor.getColumnIndex("o3")),
                        aqCursor.getString(aqCursor.getColumnIndex("pm10")),
                        aqCursor.getString(aqCursor.getColumnIndex("pm25")),
                        aqCursor.getString(aqCursor.getColumnIndex("qlty")),
                        aqCursor.getString(aqCursor.getColumnIndex("so2"))
                );
                if (!aqCursor.moveToNext()) {
                    break;
                }
            }
        }
        aqCursor.close();

        if (lsCursor.moveToFirst()) {
            for (Weather weather : weatherList) {
                weather.getLifeSuggestion().setExtraValues(
                        lsCursor.getString(lsCursor.getColumnIndex("comfort")),
                        lsCursor.getString(lsCursor.getColumnIndex("dressing")),
                        lsCursor.getString(lsCursor.getColumnIndex("uv_ray")),
                        lsCursor.getString(lsCursor.getColumnIndex("car_wash")),
                        lsCursor.getString(lsCursor.getColumnIndex("travel")),
                        lsCursor.getString(lsCursor.getColumnIndex("flu")),
                        lsCursor.getString(lsCursor.getColumnIndex("sport"))
                );
                if (!lsCursor.moveToNext()) {
                    break;
                }
            }
        }
        lsCursor.close();

        return weatherList;
    }

    public void updateWeather(Weather weather) {
        if (weather != null) {
            ContentValues values = new ContentValues();
            if (weather.getBasicInfo() != null) {
                convert(weather.getBasicInfo(), values);
                database.update("basic_info", values, "city_id = ?", new String[]{weather.getBasicInfo().getCityId()});
                values.clear();
            }
            if (weather.getCurrentInfo() != null) {
                convert(weather.getCurrentInfo(), values);
                database.update("current_info", values, "city_id = ?", new String[]{weather.getCurrentInfo().getCityId()});
                values.clear();
            }
            if (weather.getHourlyForecastList() != null) {
                for (HourlyForecast hf : weather.getHourlyForecastList()) {
                    convert(hf, values);
                    database.update("hourly_forecast", values, "city_time_id = ?", new String[]{hf.getCityTimeId()});
                    values.clear();
                }
            }
            if (weather.getDailyForecastList() != null) {
                for (DailyForecast df : weather.getDailyForecastList()) {
                    convert(df, values);
                    database.update("daily_forecast", values, "city_date_id = ?", new String[]{df.getCityDateId()});
                    values.clear();
                }
            }
            if (weather.getAirQuality() != null) {
                convert(weather.getAirQuality(), values);
                database.update("air_quality", values, "city_id = ?", new String[]{weather.getAirQuality().getCityId()});
                values.clear();
            }
            if (weather.getLifeSuggestion() != null) {
                convert(weather.getLifeSuggestion(), values);
                database.update("life_suggestion", values, "city_id = ?", new String[]{weather.getLifeSuggestion().getCityId()});
                values.clear();
            }
        }
    }

    public void deleteWeather(String cityId) {
        database.delete("basic_info", "city_id = ?", new String[]{cityId});
        database.delete("current_info", "city_id = ?", new String[]{cityId});
        database.delete("hourly_forecast", "city_time_id like ?", new String[]{cityId + "%"});
        database.delete("daily_forecast", "city_date_id like ?", new String[]{cityId + "%"});
        database.delete("air_quality", "city_id = ?", new String[]{cityId});
        database.delete("life_suggestion", "city_id = ?", new String[]{cityId});
    }

    private void convert(BasicInfo bi, ContentValues values) {
        if (values.size() != 0) {
            values.clear();
        }
        values.put("city_id", bi.getCityId());
        values.put("city_name", bi.getCityName());
        values.put("update_time", bi.getUpdateTime());
    }

    private void convert(CurrentInfo ci, ContentValues values) {
        if (values.size() != 0) {
            values.clear();
        }
        values.put("city_id", ci.getCityId());
        values.put("condition", ci.getCondition());
        values.put("temperature", ci.getTemperature());
        values.put("sensible_temp", ci.getSensibleTemp());
        values.put("humidity", ci.getHumidity());
        values.put("precipitation", ci.getPrecipitation());
        values.put("pressure", ci.getPressure());
        values.put("visibility", ci.getVisibility());
        values.put("wind_speed", ci.getWindSpeed());
        values.put("wind_scale", ci.getWindScale());
        values.put("wind_deg", ci.getWindDeg());
        values.put("wind_direction", ci.getWindDirection());
    }

    private void convert(HourlyForecast hf, ContentValues values) {
        if (values.size() != 0) {
            values.clear();
        }
        values.put("city_time_id", hf.getCityTimeId());
        values.put("time", hf.getTime());
        values.put("temperature", hf.getTemperature());
        values.put("wind_speed", hf.getWindSpeed());
        values.put("wind_scale", hf.getWindScale());
        values.put("wind_deg", hf.getWindDeg());
        values.put("wind_direction", hf.getWindDirection());
        values.put("pcpn_prob", hf.getPcpnProb());
        values.put("humidity", hf.getHumidity());
        values.put("pressure", hf.getPressure());
    }

    private void convert(DailyForecast df, ContentValues values) {
        if (values.size() != 0) {
            values.clear();
        }
        values.put("city_date_id", df.getCityDateId());
        values.put("date", df.getDate());
        values.put("sunrise_time", df.getSunriseTime());
        values.put("sunset_time", df.getSunsetTime());
        values.put("max_temp", df.getMaxTemp());
        values.put("min_temp", df.getMinTemp());
        values.put("wind_speed", df.getWindSpeed());
        values.put("wind_scale", df.getWindScale());
        values.put("wind_deg", df.getWindDeg());
        values.put("wind_direction", df.getWindDirection());
        values.put("condition_day", df.getConditionDay());
        values.put("condition_night", df.getConditionNight());
        values.put("precipitation", df.getPrecipitation());
        values.put("pcpn_prob", df.getPcpnProb());
        values.put("humidity", df.getHumidity());
        values.put("pressure", df.getPressure());
        values.put("visibility", df.getVisibility());
    }

    private void convert(AirQuality aq, ContentValues values) {
        if (values.size() != 0) {
            values.clear();
        }
        values.put("city_id", aq.getCityId());
        values.put("aqi", aq.getAqi());
        values.put("co", aq.getPm10());
        values.put("no2", aq.getPm25());
        values.put("o3", aq.getAqi());
        values.put("pm10", aq.getPm10());
        values.put("pm25", aq.getPm25());
        values.put("qlty", aq.getPm10());
        values.put("so2", aq.getPm25());
    }

    private void convert(LifeSuggestion ls, ContentValues values) {
        if (values.size() != 0) {
            values.clear();
        }
        values.put("city_id", ls.getCityId());
        values.put("comfort", ls.getComfort());
        values.put("dressing", ls.getDressing());
        values.put("uv_ray", ls.getUvRay());
        values.put("car_wash", ls.getCarWash());
        values.put("travel", ls.getTravel());
        values.put("flu", ls.getFlu());
        values.put("sport", ls.getSport());
    }
}
