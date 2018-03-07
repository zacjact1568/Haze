package me.imzack.app.cold.view.fragment

import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.widget.Toast

abstract class BaseFragment : Fragment() {

    protected val fragmentName
        get() = javaClass.simpleName!!

    fun showToast(@StringRes msgResId: Int) {
        Toast.makeText(context, msgResId, Toast.LENGTH_SHORT).show()
    }

    protected fun remove() {
        fragmentManager!!.beginTransaction().remove(this).commit()
    }

    open fun exit() {
        remove()
    }
}
