package me.imzack.app.cold.presenter

import me.imzack.app.cold.model.DataManager
import me.imzack.app.cold.view.contract.GuideViewContract

class GuidePresenter(private var guideViewContract: GuideViewContract?) : BasePresenter() {

    override fun attach() {
        guideViewContract!!.showInitialView()
    }

    override fun detach() {
        guideViewContract = null
    }

    fun notifyEndingGuide(isNormally: Boolean) {
        if (isNormally) {
            DataManager.preferenceHelper.needGuideValue = false
        }
        guideViewContract!!.exitWithResult(isNormally)
    }
}