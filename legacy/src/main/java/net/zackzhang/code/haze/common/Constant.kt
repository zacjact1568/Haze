package net.zackzhang.code.haze.common

object Constant {

    const val BASIC = "basic"
    const val CITY_ID = "city_id"
    const val CITY_NAME = "city_name"
    const val IS_PREFECTURE = "is_prefecture"
    const val UPDATE_TIME = "update_time"
    const val ADD_TIME = "add_time"

    const val CURRENT = "current"
    const val CONDITION_CODE = "condition_code"
    const val TEMPERATURE = "temperature"
    const val FEELS_LIKE = "feels_like"
    const val AIR_QUALITY_INDEX = "air_quality_index"

    const val HOURLY_FORECAST = "hourly_forecast"
    const val SEQUENCE = "sequence"
    const val TIME = "time"
    const val PRECIPITATION_PROBABILITY = "precipitation_probability"

    const val DAILY_FORECAST = "daily_forecast"
    const val DATE = "date"
    const val TEMPERATURE_MAX = "temperature_max"
    const val TEMPERATURE_MIN = "temperature_min"
    const val CONDITION_CODE_DAY = "condition_code_day"
    const val CONDITION_CODE_NIGHT = "condition_code_night"

    const val PREF_KEY_NEED_GUIDE = "need_guide"
    const val PREF_KEY_LOCATION_SERVICE = "location_service"
    const val PREF_KEY_HAVE_REQUESTED_LOCATION_PERMISSIONS = "have_requested_location_permissions"

    const val UNKNOWN_DATA = "--"

    const val TIME_FORMAT = "yyyy-MM-dd HH:mm"
    const val DATE_FORMAT = "yyyy-MM-dd"

    const val WEATHER_LIST_POSITION = "weather_list_position"

    const val WEATHER = "weather"
    const val CITIES = "cities"
    const val SETTINGS = "settings"
    const val ABOUT = "about"
    const val GUIDE = "guide"
    const val ADD_CITY = "add_city"

    const val CURRENT_FRAGMENT = "current_fragment"

    const val HE_WEATHER_API_URL = "https://free-api.heweather.com/s6/"
    const val HE_WEATHER_SEARCH_URL = "https://search.heweather.com/"
    const val HE_WEATHER_USER_ID = "HE1605091919551466"
    const val HE_WEATHER_API_KEY = "b62ec297adac4f64a4aabdc2f86e1ce7"

    // 支持的语言
    const val EN = "en"
    const val ZH_CN = "zh_cn"
    const val ZH_TW = "zh_tw"

    // 天气情况数据库
    const val HE_WEATHER_CONDITION_DB_FN = "db_he_weather_condition.db"
    // 天气情况表
    const val CONDITION = "condition"
    // 各行名称
    const val CODE = "code"
    const val NAME_LANG = "name_%s"

    // 地区数据库
    const val HE_WEATHER_LOCATION_DB_FN = "db_he_weather_location.db"
    // 国内城市表
    const val CHINA_CITY = "china_city"
    // 各行名称
    const val ID = "id"
    const val NAME_EN = "name_en"
    const val NAME_ZH_CN = "name_zh_cn"
    const val PROVINCE_ZH_CN = "province_zh_cn"
    const val PREFECTURE_ZH_CN = "prefecture_zh_cn"

    // 定位模式
    const val LOCATION_MODE_NONE = 0
    const val LOCATION_MODE_HIGH_ACCURACY = 1
    const val LOCATION_MODE_BATTERY_SAVING = 2
    const val LOCATION_MODE_DEVICE_SENSORS = 3

    const val CITY_ID_CURRENT_LOCATION = "current_location"

    const val DEVELOPER_EMAIL = "zxjue@outlook.com"

    const val APP_BAR_STATE_EXPANDED = 0
    const val APP_BAR_STATE_INTERMEDIATE = 1
    const val APP_BAR_STATE_COLLAPSED = 2
}