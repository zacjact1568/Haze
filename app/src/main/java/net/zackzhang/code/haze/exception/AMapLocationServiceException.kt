package net.zackzhang.code.haze.exception

class AMapLocationServiceException(val code: Int, info: String) : Exception("AMap Location Service reported an error: ($code) $info")