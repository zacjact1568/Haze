package me.imzack.app.cold.view.activity

import android.support.annotation.StringRes
import android.support.v7.app.AppCompatActivity
import android.widget.Toast

abstract class BaseActivity : AppCompatActivity() {

    protected val activityName
        get() = javaClass.simpleName!!

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
