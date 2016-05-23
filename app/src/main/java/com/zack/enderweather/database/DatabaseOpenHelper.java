package com.zack.enderweather.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

    /** 基本信息 */
    private static final String CREATE_TABLE_BASIC_INFO = "create table basic_info (" +
            "city_id text primary key, " +
            "city_name text, " +
            "update_time text)";

    /** 实况天气 */
    private static final String CREATE_TABLE_CURRENT_INFO = "create table current_info (" +
            "city_id text primary key, " +
            "condition text, " +
            "temperature integer, " +
            "sensible_temp integer, " +
            "humidity integer, " +
            "precipitation integer, " +
            "pressure integer, " +
            "visibility integer, " +
            "wind_speed integer, " +
            "wind_scale text, " +
            "wind_deg integer, " +
            "wind_direction text)";

    /** 每小时天气预报 */
    private static final String CREATE_TABLE_HOURLY_FORECAST = "create table hourly_forecast (" +
            "city_id text, " +
            "time text, " +
            "temperature integer, " +
            "wind_speed integer, " +
            "wind_scale text, " +
            "wind_deg integer, " +
            "wind_direction text, " +
            "pcpn_prob integer" +
            "humidity integer, " +
            "pressure integer)";

    /** 每日天气预报 */
    private static final String CREATE_TABLE_DAILY_FORECAST = "create table daily_forecast (" +
            "city_id text, " +
            "date text, " +
            "sunrise_time text, " +
            "sunset_time text, " +
            "max_temp integer, " +
            "min_temp integer, " +
            "wind_speed integer, " +
            "wind_scale text, " +
            "wind_deg integer, " +
            "wind_direction text, " +
            "condition_day text, " +
            "condition_night text, " +
            "precipitation integer, " +
            "pcpn_prob integer" +
            "humidity integer, " +
            "pressure integer, " +
            "visibility integer)";

    /** 空气质量 */
    private static final String CREATE_TABLE_AIR_QUALITY = "create table air_quality (" +
            "city_id text primary key, " +
            "aqi integer, " +
            "pm10 integer, " +
            "pm25 integer)";

    /** 生活建议 */
    private static final String CREATE_TABLE_LIFE_SUGGESTION = "create table life_suggestion (" +
            "city_id text primary key, " +
            "comfort text, " +
            "dressing text, " +
            "uv_ray text, " +
            "car_wash text, " +
            "travel text, " +
            "flu text, " +
            "sport text)";

    public DatabaseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_BASIC_INFO);
        db.execSQL(CREATE_TABLE_CURRENT_INFO);
        db.execSQL(CREATE_TABLE_HOURLY_FORECAST);
        db.execSQL(CREATE_TABLE_DAILY_FORECAST);
        db.execSQL(CREATE_TABLE_AIR_QUALITY);
        db.execSQL(CREATE_TABLE_LIFE_SUGGESTION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
