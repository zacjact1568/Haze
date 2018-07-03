package net.zackzhang.app.cold.presenter

import net.zackzhang.app.cold.model.DataManager
import net.zackzhang.app.cold.view.contract.GuideViewContract

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