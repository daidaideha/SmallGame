package com.lyl.base.interfaces

/**
 * create lyl on 2019-08-13
 *
 */
interface ISnakeCallback {

    fun reDraw()

    fun outBorderLine()

    fun score(score: Int)

    fun onPause()
}
