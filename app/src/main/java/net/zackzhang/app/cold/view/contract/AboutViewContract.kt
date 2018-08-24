package net.zackzhang.app.cold.view.contract

interface AboutViewContract : BaseViewContract {

    fun showInitialView()

    fun onAppBarScrolled(headerLayoutAlpha: Float)

    fun onAppBarScrolledToCriticalPoint(toolbarTitle: String)
}