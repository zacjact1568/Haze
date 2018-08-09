package net.zackzhang.app.cold.view.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import net.zackzhang.app.cold.R
import net.zackzhang.app.cold.presenter.GuidePresenter
import net.zackzhang.app.cold.util.ResourceUtil
import net.zackzhang.app.cold.util.StringUtil
import net.zackzhang.app.cold.view.contract.GuideViewContract
import net.zackzhang.app.cold.view.fragment.LocationGuidePageFragment
import net.zackzhang.lib.baseguideactivity.BaseGuideActivity
import net.zackzhang.lib.baseguideactivity.SimpleGuidePageFragment

class GuideActivity : BaseGuideActivity(), GuideViewContract {

    companion object {

        fun start(activity: Activity) {
            activity.startActivityForResult(Intent(activity, GuideActivity::class.java), 0)
        }
    }

    private val guidePresenter = GuidePresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        guidePresenter.attach()
    }

    override fun getPageFragmentList() = listOf(
            SimpleGuidePageFragment.Builder()
                    .setImage(R.drawable.ic_launcher_no_padding, Color.WHITE)
                    .setTitle(StringUtil.addWhiteColorSpan(getString(R.string.title_page_welcome)))
                    .setDescription(StringUtil.addWhiteColorSpan(getString(R.string.dscpt_page_welcome)))
                    .build(),
            LocationGuidePageFragment.newInstance(),
            SimpleGuidePageFragment.Builder()
                    .setImage(R.drawable.ic_check_black_24dp, Color.WHITE)
                    .setTitle(StringUtil.addWhiteColorSpan(getString(R.string.title_page_ready)))
                    .setDescription(StringUtil.addWhiteColorSpan(getString(R.string.dscpt_page_ready)))
                    .build()
    )

    override fun onBackPressedOnce() {
        showToast(R.string.toast_double_press_exit)
    }

    override fun onBackPressedTwice() {
        guidePresenter.notifyEndingGuide(false)
    }

    override fun onLastPageTurned() {
        guidePresenter.notifyEndingGuide(true)
    }

    override fun onDestroy() {
        super.onDestroy()
        guidePresenter.detach()
    }

    override fun showInitialView() {
        setBackgroundColor(ResourceUtil.getColor(R.color.colorPrimary))
        setStartButtonColor(ResourceUtil.getColor(R.color.colorPrimaryDark))
        setEndButtonColor(ResourceUtil.getColor(R.color.colorPrimaryDark))
    }

    override fun exitWithResult(isNormally: Boolean) {
        setResult(if (isNormally) RESULT_OK else RESULT_CANCELED)
        exit()
    }

    override fun showToast(msgResId: Int) {
        Toast.makeText(this, msgResId, Toast.LENGTH_SHORT).show()
    }

    override fun exit() {
        finish()
    }
}
