package com.lyl.game

import com.lyl.game.impl.SnakeImpl
import com.lyl.game.interfaces.IGameBody
import com.lyl.game.interfaces.ISnakeCallback

/**
 * create lyl on 2019-08-16
 *
 */
object GameBodyFactory {

    const val GAME_SNAKE = 1

    fun createGameBody(type: Int, callback: ISnakeCallback): IGameBody? {
        var gameBody: IGameBody? = null
        when (type) {
            GAME_SNAKE -> gameBody = SnakeImpl(callback)
            else -> {
            }
        }
        return gameBody
    }
}
