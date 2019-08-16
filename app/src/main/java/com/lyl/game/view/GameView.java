package com.lyl.game.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.lyl.game.enums.GameDirection;
import com.lyl.game.enums.GameOperate;
import com.lyl.game.interfaces.IGameAction;
import com.lyl.game.interfaces.IGameBody;
import com.lyl.game.utils.DensityUtil;

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
    public void destroy() {
        if (gameBody != null) gameBody.destroy();
    }

    @Override
    public void actionDirection(GameDirection direction) {
        if (gameBody != null) gameBody.actionDirection(direction);
    }

    @Override
    public void actionOperate(GameOperate operate) {
        if (gameBody != null) gameBody.actionOperate(operate);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mWidth = measureDimension(DensityUtil.getScreenWidth(), widthMeasureSpec);
        int mHeight = measureDimension(DensityUtil.getScreenWidth(), heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
    }

    /***
     * 计算控件宽高
     *
     * @param defaultSize 默认大小
     * @param measureSpec
     * @return 最终大小
     */
    private int measureDimension(int defaultSize, int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            result = Math.min(defaultSize, specSize);
        } else {
            result = defaultSize;
        }
        return result;
    }
}
