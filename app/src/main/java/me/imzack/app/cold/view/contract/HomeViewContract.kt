package me.imzack.app.cold.view.contract

interface HomeViewContract : BaseViewContract {

    fun showInitialView()

    fun showInitialFragment(restored: Boolean, shownTag: String)

    fun showToast(msg: String)

    fun switchFragment(fromTag: String, toTag: String)

    fun startActivity(tag: String)

    fun closeDrawer()

    fun onPressBackKey()

    fun onDetectedLocationServicePermissionsDenied()

    fun onDetectedNetworkNotAvailable()

    fun onDetectedSystemLocationServiceDisabled()
}
