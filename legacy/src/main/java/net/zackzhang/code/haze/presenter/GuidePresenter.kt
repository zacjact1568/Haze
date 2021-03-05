package net.zackzhang.code.haze.presenter

import net.zackzhang.code.haze.model.DataManager
import net.zackzhang.code.haze.view.contract.GuideViewContract

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