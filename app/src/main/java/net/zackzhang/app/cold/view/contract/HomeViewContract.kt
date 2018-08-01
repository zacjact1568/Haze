package net.zackzhang.app.cold.view.contract

interface HomeViewContract : BaseViewContract {

    fun showInitialView(currentFragmentTag: String, isCityEmpty: Boolean)

    fun showInitialFragment(restored: Boolean, shownTag: String, isCityEmpty: Boolean)

    fun showToast(msg: String)

    fun switchFragment(fromTag: String, toTag: String, isCityEmpty: Boolean)

    fun startActivity(tag: String)

    fun closeDrawer()

    fun onPressBackKey()

    fun onDetectedNetworkNotAvailable()

    fun onDetectedSystemLocationServiceDisabled()

    fun onDetectedNoEnoughPermissionsGranted()

    fun onLocationServicePermissionsDenied()

    fun onCityEmptyStateChanged(isEmpty: Boolean, currentFragmentTag: String)
}
