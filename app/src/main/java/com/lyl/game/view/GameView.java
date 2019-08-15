package com.lyl.game.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.lyl.game.enums.GameDirection;
import com.lyl.game.enums.GameOpreate;
import com.lyl.game.interfaces.IGameAction;
import com.lyl.game.interfaces.IGameBody;

/**
 * create lyl on 2019-08-13
 * </p>
 * 游戏视图
 */
public class GameView extends View implements IGameAction {

    private IGameBody gameBody;

    public GameView(Context context) {
        super(context);
    }

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setGameBody(IGameBody gameBody) {
        this.gameBody = gameBody;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (gameBody != null) gameBody.drawGames(canvas);
    }

    @Override
    public void startGame() {
        if (gameBody != null) gameBody.startGame();
    }

    @Override
    public void actionDirection(GameDirection direction) {
        if (gameBody != null) gameBody.actionDirection(direction);
    }

    @Override
    public void actionOperate(GameOpreate... operate) {
        if (gameBody != null) gameBody.actionOperate(operate);
    }
}
