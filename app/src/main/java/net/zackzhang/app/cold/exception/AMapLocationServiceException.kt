package net.zackzhang.app.cold.exception

class AMapLocationServiceException(val code: Int, info: String) : Exception("AMap Location Service reported an error: ($code) $info")