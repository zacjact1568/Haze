package net.zackzhang.code.haze.view.activity

import android.support.annotation.StringRes
import android.support.v7.app.AppCompatActivity
import android.widget.Toast

abstract class BaseActivity : AppCompatActivity() {

    protected fun setupActionBar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun showToast(@StringRes msgResId: Int) {
        Toast.makeText(this, msgResId, Toast.LENGTH_SHORT).show()
    }

    open fun exit() {
        finish()
    }
}
