package net.zackzhang.code.haze.common.constant

const val CARD_TYPE_WEATHER_HEAD = 0
const val CARD_TYPE_WEATHER_HOURLY_ROW = 1
const val CARD_TYPE_WEATHER_TITLE = 2
const val CARD_TYPE_WEATHER_DAILY = 3
const val CARD_TYPE_WEATHER_CURRENT = 4
const val CARD_TYPE_CITY_SEARCH_ASSOCIATION = 10
const val CARD_TYPE_WEATHER_HOURLY = 11
const val CARD_TYPE_SETTINGS_SWITCH_PREFERENCE = 12
const val CARD_TYPE_SETTINGS_INFO_PREFERENCE = 13
const val CARD_TYPE_SOURCE = 20
const val CARD_TYPE_SPACE = 30

const val RESULT_CODE_CITY_NEW = 20

/**
 * 本地数据库缓存中的城市加载通知
 * 用于 WeatherFragment -> HomeActivity
 */
const val EVENT_CITY_LOADED = "city_loaded"

/**
 * 城市更改通知
 * 用于 HomeActivity -> WeatherFragment
 */
const val EVENT_CITY_CHANGED = "city_changed"
const val EVENT_ACTIVITY_FINISH = "activity_finish"
const val EVENT_CITY_SELECTED = "city_selected"
const val EVENT_THEME_CHANGED = "theme_changed"
const val EVENT_WINDOW_INSETS_APPLIED = "window_insets_applied"
const val EVENT_NETWORK_FAILED = "network_failed"

const val EXTRA_THEME = "theme"

const val PREFERENCE_KEY_SHOW_CURRENT_ZONE_TIME = "show_current_zone_time"
const val PREFERENCE_KEY_ABOUT = "about"