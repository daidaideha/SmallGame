package com.lyl.game.interfaces;

import com.lyl.game.enums.GameDirection;
import com.lyl.game.enums.GameOperate;

/**
 * create lyl on 2019-08-13
 * </p>
 */
public interface IGameAction {

//    /**
//     * 开始游戏
//     */
//    void startGame();

    /**
     * 响应方向
     */
    void actionDirection(GameDirection direction);

    /**
     * 响应操作
     */
    void actionOperate(GameOperate... operate);
}
