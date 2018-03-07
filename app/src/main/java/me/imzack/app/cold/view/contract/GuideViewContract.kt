package me.imzack.app.cold.view.contract

interface GuideViewContract : BaseViewContract {

    fun showInitialView()

    fun exitWithResult(isNormally: Boolean)
}