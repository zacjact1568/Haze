package net.zackzhang.app.cold.presenter

import net.zackzhang.app.cold.R
import net.zackzhang.app.cold.common.Constant
import net.zackzhang.app.cold.util.ResourceUtil
import net.zackzhang.app.cold.view.contract.AboutViewContract

class AboutPresenter(private var aboutViewContract: AboutViewContract?) : BasePresenter() {

    private var appBarMaxRange = 0
    private var lastHeaderAlpha = 1f
    private var appBarState = Constant.APP_BAR_STATE_EXPANDED

    override fun attach() {
        aboutViewContract!!.showInitialView()
    }

    override fun detach() {
        aboutViewContract = null
    }

    fun notifyPreDrawingAppBar(maxRange: Int) {
        appBarMaxRange = maxRange
    }

    fun notifyAppBarScrolled(offset: Int) {

        if (appBarMaxRange == 0) return

        val absOffset = Math.abs(offset)
        var headerAlpha = 1f - absOffset * 1.3f / appBarMaxRange
        if (headerAlpha < 0) headerAlpha = 0f

        if ((headerAlpha == 0f || lastHeaderAlpha == 0f) && headerAlpha != lastHeaderAlpha) {
            aboutViewContract!!.onAppBarScrolledToCriticalPoint(if (headerAlpha == 0f) ResourceUtil.getString(R.string.title_activity_about) else " ")
            lastHeaderAlpha = headerAlpha
        }

        aboutViewContract!!.onAppBarScrolled(headerAlpha)

        appBarState = when (absOffset) {
            0 -> Constant.APP_BAR_STATE_EXPANDED
            appBarMaxRange -> Constant.APP_BAR_STATE_COLLAPSED
            else -> Constant.APP_BAR_STATE_INTERMEDIATE
        }
    }
}