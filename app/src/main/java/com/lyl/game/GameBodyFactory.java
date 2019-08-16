package com.lyl.game;

import com.lyl.game.impl.SnakeImpl;
import com.lyl.game.interfaces.IGameBody;
import com.lyl.game.interfaces.ISnakeCallback;

/**
 * create lyl on 2019-08-16
 * </p>
 */
public class GameBodyFactory {

    public static final int GAME_SNAKE = 1;

    public static IGameBody createGameBody(int type, ISnakeCallback callback) {
        IGameBody gameBody = null;
        switch (type) {
            case GAME_SNAKE:
                gameBody = new SnakeImpl(callback);
                break;
            case 2:
                break;
            default:
                break;
        }
        return gameBody;
    }
}
