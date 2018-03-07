package me.imzack.app.cold.model.database

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

import me.imzack.app.cold.App
import me.imzack.app.cold.common.Constant
import me.imzack.app.cold.model.bean.*
import me.imzack.app.cold.util.SystemUtil

class DatabaseHelper {

    private val DB_NAME = "${App.context.packageName}.db"

    private val DB_VERSION = 1

    private val database = DatabaseOpenHelper(App.context, DB_NAME, null, DB_VERSION).writableDatabase

    // ***************** Weather *****************

    fun loadWeatherAsync(callback: (List<Weather>) -> Unit) {
        Observable.create(ObservableOnSubscribe<List<Weather>> { it.onNext(loadWeather()) })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { callback(it) }
    }

    private fun loadWeather(): List<Weather> {
        val weatherList = mutableListOf<Weather>()

        val basicCursor = database.rawQuery("select * from ${Constant.BASIC}", null)
        val currentCursor = database.rawQuery("select * from ${Constant.CURRENT}", null)
        val hourlyForecastCursor = database.rawQuery("select * from ${Constant.HOURLY_FORECAST}", null)
        val dailyForecastCursor = database.rawQuery("select * from ${Constant.DAILY_FORECAST}", null)

        if (basicCursor.moveToFirst()) {
            do {
                val weather = Weather(
                        basicCursor.getString(basicCursor.getColumnIndex(Constant.CITY_ID)),
                        basicCursor.getString(basicCursor.getColumnIndex(Constant.CITY_NAME))
                )
                weather.basic.updateTime = basicCursor.getLong(basicCursor.getColumnIndex(Constant.UPDATE_TIME))
                weatherList.add(weather)
            } while (basicCursor.moveToNext())
        }
        basicCursor.close()

        if (currentCursor.moveToFirst()) {
            for (weather in weatherList) {
                val current = weather.current
                current.conditionCode = currentCursor.getInt(currentCursor.getColumnIndex(Constant.CONDITION_CODE))
                current.temperature = currentCursor.getInt(currentCursor.getColumnIndex(Constant.TEMPERATURE))
                current.feelsLike = currentCursor.getInt(currentCursor.getColumnIndex(Constant.FEELS_LIKE))
                current.airQualityIndex = currentCursor.getInt(currentCursor.getColumnIndex(Constant.AIR_QUALITY_INDEX))
                if (!currentCursor.moveToNext()) break
            }
        }
        currentCursor.close()

        if (hourlyForecastCursor.moveToFirst()) {
            for (weather in weatherList) {
                for (hourlyForecast in weather.hourlyForecasts) {
                    hourlyForecast.time = hourlyForecastCursor.getLong(hourlyForecastCursor.getColumnIndex(Constant.TIME))
                    hourlyForecast.temperature = hourlyForecastCursor.getInt(hourlyForecastCursor.getColumnIndex(Constant.TEMPERATURE))
                    hourlyForecast.precipitationProbability = hourlyForecastCursor.getInt(hourlyForecastCursor.getColumnIndex(Constant.PRECIPITATION_PROBABILITY))
                    if (!hourlyForecastCursor.moveToNext()) break
                }
            }
        }
        hourlyForecastCursor.close()

        if (dailyForecastCursor.moveToFirst()) {
            for (weather in weatherList) {
                for (dailyForecast in weather.dailyForecasts) {
                    dailyForecast.date = dailyForecastCursor.getLong(dailyForecastCursor.getColumnIndex(Constant.DATE))
                    dailyForecast.temperatureMax = dailyForecastCursor.getInt(dailyForecastCursor.getColumnIndex(Constant.TEMPERATURE_MAX))
                    dailyForecast.temperatureMin = dailyForecastCursor.getInt(dailyForecastCursor.getColumnIndex(Constant.TEMPERATURE_MIN))
                    dailyForecast.conditionCodeDay = dailyForecastCursor.getInt(dailyForecastCursor.getColumnIndex(Constant.CONDITION_CODE_DAY))
                    dailyForecast.conditionCodeNight = dailyForecastCursor.getInt(dailyForecastCursor.getColumnIndex(Constant.CONDITION_CODE_NIGHT))
                    dailyForecast.precipitationProbability = dailyForecastCursor.getInt(dailyForecastCursor.getColumnIndex(Constant.PRECIPITATION_PROBABILITY))
                    if (!dailyForecastCursor.moveToNext()) break
                }
            }
        }
        dailyForecastCursor.close()

        return weatherList
    }

    fun insertWeather(weather: Weather) {
        val values = ContentValues()

        convert(weather.basic, values)
        database.insert(Constant.BASIC, null, values)

        convert(weather.current, values)
        database.insert(Constant.CURRENT, null, values)

        for (hourlyForecast in weather.hourlyForecasts) {
            convert(hourlyForecast, values)
            database.insert(Constant.HOURLY_FORECAST, null, values)
        }

        for (dailyForecast in weather.dailyForecasts) {
            convert(dailyForecast, values)
            database.insert(Constant.DAILY_FORECAST, null, values)
        }
    }

    fun updateWeather(weather: Weather) {
        val values = ContentValues()

        convert(weather.basic, values)
        database.update(Constant.BASIC, values, "${Constant.CITY_ID} = ?", arrayOf(weather.basic.cityId))

        convert(weather.current, values)
        database.update(Constant.CURRENT, values, "${Constant.CITY_ID} = ?", arrayOf(weather.current.cityId))

        for (hourlyForecast in weather.hourlyForecasts) {
            convert(hourlyForecast, values)
            database.update(Constant.HOURLY_FORECAST, values, "${Constant.CITY_ID} = ? and ${Constant.SEQUENCE} = ?", arrayOf(hourlyForecast.cityId, hourlyForecast.sequence.toString()))
        }

        for (dailyForecast in weather.dailyForecasts) {
            convert(dailyForecast, values)
            database.update(Constant.DAILY_FORECAST, values, "${Constant.CITY_ID} = ? and ${Constant.SEQUENCE} = ?", arrayOf(dailyForecast.cityId, dailyForecast.sequence.toString()))
        }
    }

    fun deleteWeather(cityId: String) {
        database.delete(Constant.BASIC, "${Constant.CITY_ID} = ?", arrayOf(cityId))
        database.delete(Constant.CURRENT, "${Constant.CITY_ID} = ?", arrayOf(cityId))
        database.delete(Constant.HOURLY_FORECAST, "${Constant.CITY_ID} = ?", arrayOf(cityId))
        database.delete(Constant.DAILY_FORECAST, "${Constant.CITY_ID} = ?", arrayOf(cityId))
    }

    private fun convert(basic: Basic, values: ContentValues) {
        values.clear()
        values.put(Constant.CITY_ID, basic.cityId)
        values.put(Constant.CITY_NAME, basic.cityName)
        values.put(Constant.UPDATE_TIME, basic.updateTime)
    }

    private fun convert(current: Current, values: ContentValues) {
        values.clear()
        values.put(Constant.CITY_ID, current.cityId)
        values.put(Constant.CONDITION_CODE, current.conditionCode)
        values.put(Constant.TEMPERATURE, current.temperature)
        values.put(Constant.FEELS_LIKE, current.feelsLike)
        values.put(Constant.AIR_QUALITY_INDEX, current.airQualityIndex)
    }

    private fun convert(hourlyForecast: HourlyForecast, values: ContentValues) {
        values.clear()
        values.put(Constant.CITY_ID, hourlyForecast.cityId)
        values.put(Constant.SEQUENCE, hourlyForecast.sequence)
        values.put(Constant.TIME, hourlyForecast.time)
        values.put(Constant.TEMPERATURE, hourlyForecast.temperature)
        values.put(Constant.PRECIPITATION_PROBABILITY, hourlyForecast.precipitationProbability)
    }

    private fun convert(dailyForecast: DailyForecast, values: ContentValues) {
        values.clear()
        values.put(Constant.CITY_ID, dailyForecast.cityId)
        values.put(Constant.SEQUENCE, dailyForecast.sequence)
        values.put(Constant.DATE, dailyForecast.date)
        values.put(Constant.TEMPERATURE_MAX, dailyForecast.temperatureMax)
        values.put(Constant.TEMPERATURE_MIN, dailyForecast.temperatureMin)
        values.put(Constant.CONDITION_CODE_DAY, dailyForecast.conditionCodeDay)
        values.put(Constant.CONDITION_CODE_NIGHT, dailyForecast.conditionCodeNight)
        values.put(Constant.PRECIPITATION_PROBABILITY, dailyForecast.precipitationProbability)
    }

    // ***************** City *****************

    /**
     * 从数据库中查询城市详细信息（暂时只支持国内城市，查询出中文结果）
     * @param name 输入的城市名称，中文或英文
     */
    fun queryCityLike(name: String, resultList: MutableList<City>) {
        val db = SQLiteDatabase.openDatabase(App.context.getDatabasePath(Constant.HE_WEATHER_LOCATION_DB_FN).path, null, SQLiteDatabase.OPEN_READONLY)
        val cursor = db.rawQuery("select ${Constant.ID}, ${Constant.NAME_ZH_CN}, ${Constant.PROVINCE_ZH_CN}, ${Constant.PREFECTURE_ZH_CN} from ${Constant.CHINA_CITY} where (${Constant.NAME_EN} like ?) or (${Constant.NAME_ZH_CN} like ?)", arrayOf("$name%", "$name%"))
        if (cursor.moveToFirst()) {
            do {
                resultList.add(City(
                        cursor.getString(cursor.getColumnIndex(Constant.ID)),
                        cursor.getString(cursor.getColumnIndex(Constant.NAME_ZH_CN)),
                        cursor.getString(cursor.getColumnIndex(Constant.PREFECTURE_ZH_CN)),
                        cursor.getString(cursor.getColumnIndex(Constant.PROVINCE_ZH_CN))
                ))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
    }

    // ***************** Condition *****************

    /**
     * 使用天气情况代码，从数据库中查询对应的描述文字
     * @param code 输入的城市名称，中文或英文
     */
    fun queryConditionByCode(code: Int): String? {
        val db = SQLiteDatabase.openDatabase(App.context.getDatabasePath(Constant.HE_WEATHER_CONDITION_DB_FN).path, null, SQLiteDatabase.OPEN_READONLY)
        val nameColumn = String.format(Constant.NAME_LANG, SystemUtil.preferredLanguage)
        val cursor = db.rawQuery("select $nameColumn from ${Constant.CONDITION} where ${Constant.CODE} = ?", arrayOf("$code"))
        var condition: String? = null
        if (cursor.moveToFirst()) {
            condition = cursor.getString(cursor.getColumnIndex(nameColumn))
        }
        cursor.close()
        db.close()
        return condition
    }
}
