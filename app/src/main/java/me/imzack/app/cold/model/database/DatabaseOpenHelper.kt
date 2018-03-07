package me.imzack.app.cold.model.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import me.imzack.app.cold.common.Constant

class DatabaseOpenHelper(context: Context, name: String, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, name, factory, version) {

    /** 基本信息 */
    private val _createBasicTable = "create table ${Constant.BASIC} (" +
            "${Constant.CITY_ID} text primary key, " +
            "${Constant.CITY_NAME} text, " +
            "${Constant.UPDATE_TIME} integer)"

    /** 实况天气 */
    private val _createCurrentTable = "create table ${Constant.CURRENT} (" +
            "${Constant.CITY_ID} text primary key, " +
            "${Constant.CONDITION_CODE} integer, " +
            "${Constant.TEMPERATURE} integer, " +
            "${Constant.FEELS_LIKE} integer, " +
            "${Constant.AIR_QUALITY_INDEX} integer)"

    /** 每小时天气预报 */
    private val _createHourlyForecastTable = "create table ${Constant.HOURLY_FORECAST} (" +
            "${Constant.CITY_ID} text, " +
            "${Constant.SEQUENCE} integer, " +
            "${Constant.TIME} integer, " +
            "${Constant.TEMPERATURE} integer, " +
            "${Constant.PRECIPITATION_PROBABILITY} integer, " +
            "primary key (${Constant.CITY_ID}, ${Constant.SEQUENCE}))"

    /** 每日天气预报 */
    private val _createDailyForecastTable = "create table ${Constant.DAILY_FORECAST} (" +
            "${Constant.CITY_ID} text, " +
            "${Constant.SEQUENCE} integer, " +
            "${Constant.DATE} integer, " +
            "${Constant.TEMPERATURE_MAX} integer, " +
            "${Constant.TEMPERATURE_MIN} integer, " +
            "${Constant.CONDITION_CODE_DAY} integer, " +
            "${Constant.CONDITION_CODE_NIGHT} integer, " +
            "${Constant.PRECIPITATION_PROBABILITY} integer, " +
            "primary key (${Constant.CITY_ID}, ${Constant.SEQUENCE}))"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(_createBasicTable)
        db.execSQL(_createCurrentTable)
        db.execSQL(_createHourlyForecastTable)
        db.execSQL(_createDailyForecastTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }
}
