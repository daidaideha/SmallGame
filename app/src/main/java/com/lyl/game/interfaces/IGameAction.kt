package com.lyl.game.interfaces

import com.lyl.game.enums.GameDirection
import com.lyl.game.enums.GameOperate

/**
 * create lyl on 2019-08-13
 *
 */
interface IGameAction {

    /**
     * 销毁
     */
    fun destroy()

    /**
     * 响应方向
     */
    fun actionDirection(direction: GameDirection)

    /**
     * 响应操作
     */
    fun actionOperate(operate: GameOperate)
}
