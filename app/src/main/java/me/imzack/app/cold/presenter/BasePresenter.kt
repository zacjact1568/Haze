package me.imzack.app.cold.presenter

abstract class BasePresenter {

    abstract fun attach()

    abstract fun detach()

    protected val presenterName
        get() = javaClass.simpleName!!
}
