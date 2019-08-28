package com.lyl.base.interfaces

import com.lyl.base.enums.GameDirection
import com.lyl.base.enums.GameOperate

/**
 * create lyl on 2019-08-13
 *
 */
interface IGameAction {

    /**
     * 响应方向
     */
    fun actionDirection(direction: GameDirection)

    /**
     * 响应操作
     */
    fun actionOperate(operate: GameOperate)

    /**
     * 销毁
     */
    fun destroy()
}
