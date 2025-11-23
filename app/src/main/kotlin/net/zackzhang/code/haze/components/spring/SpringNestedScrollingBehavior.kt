package net.zackzhang.code.haze.components.spring

interface SpringNestedScrollingBehavior {

    val scrolledPosition: Int

    fun scrollTo(position: Int)

    fun scrollBy(distance: Int)

    fun scrollBack()

    fun flingBack()
}