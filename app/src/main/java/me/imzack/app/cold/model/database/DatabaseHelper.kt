package me.imzack.app.cold.model.database

import android.arch.persistence.room.Room
import android.database.sqlite.SQLiteDatabase
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function4
import io.reactivex.schedulers.Schedulers
import me.imzack.app.cold.App
import me.imzack.app.cold.common.Constant
import me.imzack.app.cold.model.bean.*
import me.imzack.app.cold.model.database.entity.BasicEntity
import me.imzack.app.cold.model.database.entity.CurrentEntity
import me.imzack.app.cold.model.database.entity.DailyForecastEntity
import me.imzack.app.cold.model.database.entity.HourlyForecastEntity
import me.imzack.app.cold.util.SystemUtil

class DatabaseHelper {

    private val weatherDatabase = Room.databaseBuilder(App.context, WeatherDatabase::class.java, WeatherDatabase.NAME).allowMainThreadQueries().build()

    // ***************** Weather *****************

    fun loadWeatherAsync(callback: (List<Weather>) -> Unit) {
        Single.zip(
                weatherDatabase.basicDao().loadAll(),
                weatherDatabase.currentDao().loadAll(),
                weatherDatabase.hourlyForecastDao().loadAll(),
                weatherDatabase.dailyForecastDao().loadAll(),
                Function4<Array<BasicEntity>, Array<CurrentEntity>, Array<HourlyForecastEntity>, Array<DailyForecastEntity>, List<Weather>>
                { basicEntities, currentEntities, hourlyForecastEntities, dailyForecastEntities ->
                    val weatherList = mutableListOf<Weather>()
                    for (i in 0 until basicEntities.size) {
                        // 因为对所有表的操作都是同时进行的，各个表中各个城市的顺序一定是相同的，可以直接按序处理
                        val (cityId, cityName, longitude, latitude, updateTime) = basicEntities[i]
                        val (_, conditionCode, currentTemperature, feelsLike, airQualityIndex) = currentEntities[i]
                        weatherList.add(Weather(
                                City(cityId, cityName, longitude, latitude),
                                Weather.Current(conditionCode, currentTemperature, feelsLike, airQualityIndex),
                                Array(Weather.HOURLY_FORECAST_LENGTH) {
                                    val (_, _, time, hourlyTemperature, precipitationProbability) = hourlyForecastEntities[Weather.HOURLY_FORECAST_LENGTH * i + it]
                                    Weather.HourlyForecast(time, hourlyTemperature, precipitationProbability)
                                },
                                Array(Weather.DAILY_FORECAST_LENGTH) {
                                    val (_, _, date, temperatureMax, temperatureMin, conditionCodeDay, conditionCodeNight, precipitationProbability) = dailyForecastEntities[Weather.DAILY_FORECAST_LENGTH * i + it]
                                    Weather.DailyForecast(date, temperatureMax, temperatureMin, conditionCodeDay, conditionCodeNight, precipitationProbability)
                                },
                                updateTime
                        ))
                    }
                    weatherList
                }
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                // Lambda 表达式不能直接用 it，必须指定一个参数（weatherList）
                // 这样才能推断出使用的是参数为 onSuccess 的那个方法，其类型 Consumer 接口中的方法只有一个参数，对应这里的 weatherList
                // 因为还有一个参数为 onCallback 的方法，其类型 BiConsumer 接口中的方法有两个参数
                .subscribe { weatherList -> callback(weatherList) }
    }

    fun insertWeather(weather: Weather) {
        val cityId = weather.cityId
        weatherDatabase.basicDao().insert(BasicEntity(cityId, weather.cityName, weather.city.longitude, weather.city.latitude, weather.updateTime))
        val (conditionCode, currentTemperature, feelsLike, airQualityIndex) = weather.current
        weatherDatabase.currentDao().insert(CurrentEntity(cityId, conditionCode, currentTemperature, feelsLike, airQualityIndex))
        weatherDatabase.hourlyForecastDao().insert(Array(Weather.HOURLY_FORECAST_LENGTH) {
            val (time, hourlyTemperature, precipitationProbability) = weather.hourlyForecasts[it]
            HourlyForecastEntity(cityId, it, time, hourlyTemperature, precipitationProbability)
        })
        weatherDatabase.dailyForecastDao().insert(Array(Weather.DAILY_FORECAST_LENGTH) {
            val (date, temperatureMax, temperatureMin, conditionCodeDay, conditionCodeNight, precipitationProbability) = weather.dailyForecasts[it]
            DailyForecastEntity(cityId, it, date, temperatureMax, temperatureMin, conditionCodeDay, conditionCodeNight, precipitationProbability)
        })
    }

    fun updateWeather(weather: Weather) {
        val cityId = weather.cityId
        weatherDatabase.basicDao().update(BasicEntity(cityId, weather.cityName, weather.city.longitude, weather.city.latitude, weather.updateTime))
        val (conditionCode, currentTemperature, feelsLike, airQualityIndex) = weather.current
        weatherDatabase.currentDao().update(CurrentEntity(cityId, conditionCode, currentTemperature, feelsLike, airQualityIndex))
        weatherDatabase.hourlyForecastDao().update(Array(Weather.HOURLY_FORECAST_LENGTH) {
            val (time, hourlyTemperature, precipitationProbability) = weather.hourlyForecasts[it]
            HourlyForecastEntity(cityId, it, time, hourlyTemperature, precipitationProbability)
        })
        weatherDatabase.dailyForecastDao().update(Array(Weather.DAILY_FORECAST_LENGTH) {
            val (date, temperatureMax, temperatureMin, conditionCodeDay, conditionCodeNight, precipitationProbability) = weather.dailyForecasts[it]
            DailyForecastEntity(cityId, it, date, temperatureMax, temperatureMin, conditionCodeDay, conditionCodeNight, precipitationProbability)
        })
    }

    fun deleteWeather(cityId: String) {
        weatherDatabase.basicDao().delete(cityId)
        weatherDatabase.currentDao().delete(cityId)
        weatherDatabase.hourlyForecastDao().delete(cityId)
        weatherDatabase.dailyForecastDao().delete(cityId)
    }

    // ***************** City *****************

    /**
     * 从数据库中查询城市详细信息（暂时只支持国内城市，查询出中文结果）
     * @param name 输入的城市名称，中文或英文
     */
    // TODO 结束所有查询再关闭数据库
    fun queryCityLike(name: String, resultList: MutableList<City>) {
        val db = SQLiteDatabase.openDatabase(App.context.getDatabasePath(Constant.HE_WEATHER_LOCATION_DB_FN).path, null, SQLiteDatabase.OPEN_READONLY)
        val cursor = db.rawQuery(
                "SELECT ${Constant.ID}, ${Constant.NAME_ZH_CN}, ${Constant.PROVINCE_ZH_CN}, ${Constant.PREFECTURE_ZH_CN}, ${Constant.LATITUDE}, ${Constant.LONGITUDE} FROM ${Constant.CHINA_CITY} WHERE (${Constant.NAME_EN} LIKE ?) OR (${Constant.NAME_ZH_CN} LIKE ?)",
                arrayOf("$name%", "$name%")
        )
        if (cursor.moveToFirst()) {
            do {
                resultList.add(City(
                        cursor.getString(cursor.getColumnIndex(Constant.ID)),
                        cursor.getString(cursor.getColumnIndex(Constant.NAME_ZH_CN)),
                        cursor.getDouble(cursor.getColumnIndex(Constant.LONGITUDE)),
                        cursor.getDouble(cursor.getColumnIndex(Constant.LATITUDE)),
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
        val cursor = db.rawQuery("SELECT $nameColumn FROM ${Constant.CONDITION} WHERE ${Constant.CODE} = ?", arrayOf("$code"))
        var condition: String? = null
        if (cursor.moveToFirst()) {
            condition = cursor.getString(cursor.getColumnIndex(nameColumn))
        }
        cursor.close()
        db.close()
        return condition
    }
}
