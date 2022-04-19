package net.zackzhang.code.haze.base.exception

/**
 * For exceptions that their names have not decided yet
 */
class PlaceholderException(val code: Int, info: String) :
    Exception("This a placeholder exception with ($code: $info)")