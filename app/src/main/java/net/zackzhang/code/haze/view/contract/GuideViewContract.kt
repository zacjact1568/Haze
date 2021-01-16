package net.zackzhang.code.haze.view.contract

interface GuideViewContract : BaseViewContract {

    fun showInitialView()

    fun exitWithResult(isNormally: Boolean)
}