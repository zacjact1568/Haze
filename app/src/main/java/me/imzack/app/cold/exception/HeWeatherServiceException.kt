package me.imzack.app.cold.exception

class HeWeatherServiceException(val type: String, val state: String, info: String) : Exception("HeWeather Service ($type) reported an error: ($state) $info")