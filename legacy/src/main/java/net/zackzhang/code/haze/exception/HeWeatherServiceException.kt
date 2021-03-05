package net.zackzhang.code.haze.exception

class HeWeatherServiceException(val type: String, val state: String, info: String) : Exception("HeWeather Service ($type) reported an error: ($state) $info")