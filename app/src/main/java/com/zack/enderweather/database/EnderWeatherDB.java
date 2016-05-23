package com.zack.enderweather.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zack.enderweather.application.EnderWeatherApp;
import com.zack.enderweather.bean.City;
import com.zack.enderweather.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class EnderWeatherDB {

    private static final String LOG_TAG = "EnderWeatherDB";

    public static final String DB_NAME = "com.zack.enderweather.db";
    public static final String DB_CITY_CN = "city_cn.db";

    public static final int DB_VERSION = 1;

    private static final String SQL_QUERY_CITY = "select city_id, city_zh, pref_zh, prov_zh from city where (city_zh like ?) or (city_en like ?)";

    private SQLiteDatabase database, cityDB;

    private static EnderWeatherDB ourInstance = new EnderWeatherDB();

    private EnderWeatherDB() {
        Context context = EnderWeatherApp.getGlobalContext();

        DatabaseOpenHelper dbHelper = new DatabaseOpenHelper(context, DB_NAME, null, DB_VERSION);
        database = dbHelper.getWritableDatabase();

        cityDB = SQLiteDatabase.openDatabase(context.getDatabasePath(DB_CITY_CN).getPath(), null, SQLiteDatabase.OPEN_READONLY);
    }

    public static EnderWeatherDB getInstance() {
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
}
