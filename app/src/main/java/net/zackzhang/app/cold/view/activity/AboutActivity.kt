package net.zackzhang.app.cold.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.view.MenuItem
import android.view.ViewTreeObserver
import com.google.android.flexbox.FlexboxItemDecoration
import com.google.android.flexbox.FlexboxLayoutManager
import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.content_about.*
import net.zackzhang.app.cold.R
import net.zackzhang.app.cold.common.Constant
import net.zackzhang.app.cold.presenter.AboutPresenter
import net.zackzhang.app.cold.util.SystemUtil
import net.zackzhang.app.cold.view.adapter.LibraryLabelAdapter
import net.zackzhang.app.cold.view.contract.AboutViewContract

class AboutActivity : BaseActivity(), AboutViewContract {

    companion object {

        fun start(context: Context) {
            context.startActivity(Intent(context, AboutActivity::class.java))
        }
    }

    private val aboutPresenter = AboutPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        aboutPresenter.attach()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        aboutPresenter.detach()
    }

    override fun showInitialView() {
        setContentView(R.layout.activity_about)

        setSupportActionBar(vToolbar)
        setupActionBar()

        vAppBarLayout.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                vAppBarLayout.viewTreeObserver.removeOnPreDrawListener(this)
                aboutPresenter.notifyPreDrawingAppBar(vAppBarLayout.totalScrollRange)
                return false
            }
        })

        vAppBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset -> aboutPresenter.notifyAppBarScrolled(verticalOffset) })

        vVersionText.text = SystemUtil.versionName

        vEmailItem.setOnClickListener {
            SystemUtil.sendEmail(Constant.DEVELOPER_EMAIL, getString(R.string.subject_developer_email), this)
        }

        vRateItem.setOnClickListener {
            SystemUtil.openLink("market://details?id=$packageName", this, R.string.toast_no_app_store_found)
        }

        vLibraryLabel.layoutManager = FlexboxLayoutManager(this)
        vLibraryLabel.adapter = LibraryLabelAdapter(this)
        val libraryItemDecoration = FlexboxItemDecoration(this)
        libraryItemDecoration.setDrawable(getDrawable(R.drawable.bg_library_divider))
        vLibraryLabel.addItemDecoration(libraryItemDecoration)
    }

    override fun onAppBarScrolled(headerLayoutAlpha: Float) {
        vHeaderLayout.alpha = headerLayoutAlpha
    }

    override fun onAppBarScrolledToCriticalPoint(toolbarTitle: String) {
        vToolbar.title = toolbarTitle
    }
}
