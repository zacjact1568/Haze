package net.zackzhang.app.cold.view.contract

interface GuideViewContract : BaseViewContract {

    fun showInitialView()

    fun exitWithResult(isNormally: Boolean)
}