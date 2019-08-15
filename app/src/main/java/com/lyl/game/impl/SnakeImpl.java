package com.lyl.game.impl;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;

import com.lyl.game.bean.FoodBean;
import com.lyl.game.bean.PointBean;
import com.lyl.game.enums.GameDirection;
import com.lyl.game.enums.GameOpreate;
import com.lyl.game.interfaces.IGameBody;
import com.lyl.game.interfaces.ISnakeCallback;
import com.lyl.game.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * create lyl on 2019-08-13
 * </p>
 * 贪吃蛇实现类
 */
public class SnakeImpl implements IGameBody {

    private Paint mSnakePaint, mFoodPaint, mBackgroundPaint;
    private Handler handler;
    private Runnable runnable;

    private List<PointBean> snakeBody = new ArrayList<>();
    private FoodBean foodBean;
    private ISnakeCallback callback;
    private GameDirection direction = GameDirection.Right;
    private float rowWidth;
    private int maxSize;
    private int screenSize;
    private int snakeWidth;
    private int foodWidth;
    private int speed = 500;
    private int score = 0;
    private boolean isPlaying;

    public SnakeImpl(ISnakeCallback callback) {
        this.callback = callback;
        screenSize = DensityUtil.getScreenWidth();
        if (screenSize <= 720) {
            maxSize = 20;
        } else if (screenSize <= 1080) {
            maxSize = 40;
        } else {
            maxSize = 60;
        }
        rowWidth = screenSize / (maxSize * 1.0f);
        snakeWidth = DensityUtil.dip2px(8);
        foodWidth = DensityUtil.dip2px(6);

        mFoodPaint = new Paint();
        mFoodPaint.setAntiAlias(true);
        mFoodPaint.setColor(Color.BLUE);

        mSnakePaint = new Paint();
        mSnakePaint.setAntiAlias(true);
        mSnakePaint.setColor(Color.BLACK);

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setColor(Color.GRAY);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (SnakeImpl.this.callback != null && isPlaying) {
                    SnakeImpl.this.callback.reDraw();
                    boolean eatFood = isHit(snakeBody.get(0), foodBean.getPointBean(), direction);
                    updateSnake(snakeBody, direction, eatFood);
                    if (eatFood) foodBean = createFood(snakeBody);
                    if (handler == null) return;
                    handler.postDelayed(runnable, speed <= 20 ? 20 : speed);
                }
            }
        };
    }

    @Override
    public void drawGames(Canvas canvas) {
        drawBackground(canvas);
        drawFood(canvas);
        drawSnake(canvas);
    }

    @Override
    public void startGame() {
        if (isPlaying) return;
        clearOldData();
        isPlaying = true;
        int header = new Random().nextInt(maxSize - 4) + 2;
        for (int i = 0; i < 3; i++) {
            snakeBody.add(new PointBean(header - i, header));
        }
        foodBean = createFood(snakeBody);
        runnable.run();
    }

    @Override
    public void actionDirection(GameDirection direction) {
        if (this.direction == direction) return;
        if (!isHit(snakeBody.get(0), snakeBody.get(1), direction))
            this.direction = direction;
    }

    @Override
    public void actionOperate(GameOpreate... operate) {

    }

    private void clearOldData() {
        snakeBody.clear();
        foodBean = null;
    }

    private void drawBackground(Canvas canvas) {
        canvas.drawRect(0, 0, screenSize, screenSize, mBackgroundPaint);
        for (int i = 0; i < maxSize; i++) {
            canvas.drawLine(0, i * rowWidth, screenSize, i * rowWidth, mSnakePaint);
            canvas.drawLine(i * rowWidth, 0, i * rowWidth, screenSize, mSnakePaint);
        }
    }

    private void drawFood(Canvas canvas) {
        if (foodBean != null) {
            float halfWidth = foodWidth / 2.0f;
            float x = foodBean.getPointBean().getX() * rowWidth + rowWidth / 2;
            float y = foodBean.getPointBean().getY() * rowWidth + rowWidth / 2;
            canvas.drawCircle(x, y, halfWidth, mFoodPaint);
        }
    }

    private void drawSnake(Canvas canvas) {
        if (snakeBody != null && snakeBody.size() > 0) {
            for (int i = 0; i < snakeBody.size(); i++) {
                float halfWidth = snakeWidth / 2.0f;
                float x = snakeBody.get(i).getX() * rowWidth + rowWidth / 2;
                float y = snakeBody.get(i).getY() * rowWidth + rowWidth / 2;
                if (i == 0) canvas.drawRect(x - halfWidth, y - halfWidth,
                        x + halfWidth, y + halfWidth, mSnakePaint);
                else canvas.drawCircle(x, y, halfWidth, mSnakePaint);
            }
        }
    }

    private boolean isHit(PointBean headerBean, PointBean hitBean, GameDirection direction) {
        int x = headerBean.getX();
        int y = headerBean.getY();
        switch (direction) {
            case Left:
                x--;
                break;
            case Up:
                y--;
                break;
            case Right:
                x++;
                break;
            case Down:
                y++;
                break;
            default:
                break;
        }
        return x == hitBean.getX() && y == hitBean.getY();
    }

    private boolean isOutBorderLine(int x, int y) {
        return x < 0 || y < 0 || x >= maxSize || y >= maxSize;
    }

    private boolean isHitBody(List<PointBean> snake, int x, int y) {
        boolean isHit = false;
        for (PointBean bean : snake) {
            if (bean.getX() == x && bean.getY() == y) {
                isHit = true;
                break;
            }
        }
        return isHit;
    }

    private void updateSnake(List<PointBean> snake, GameDirection direction, boolean eatFood) {
        int lastIndex = snake.size() - 1;
        PointBean first = snake.get(0);
        PointBean second = snake.get(1);
        PointBean last = snake.get(lastIndex);
        if (isHit(first, second, direction)) return;
        int x = first.getX();
        int y = first.getY();
        switch (direction) {
            case Left:
                x--;
                break;
            case Up:
                y--;
                break;
            case Right:
                x++;
                break;
            case Down:
                y++;
                break;
            default:
                break;
        }
        if (isOutBorderLine(x, y) || isHitBody(snake, x, y)) {
            if (callback != null) callback.outBorderLine();
            stopGame();
            return;
        }
        snake.remove(lastIndex);
        snake.add(0, new PointBean(x, y));
        if (eatFood) {
            snake.add(last);
            score += foodBean.getScore();
            speed = 500 - (score / 2) * 20;
            if (callback != null) callback.score(score);
        }
    }

    private FoodBean createFood(List<PointBean> snakeBody) {
        FoodBean foodBean = new FoodBean(new PointBean(new Random().nextInt(maxSize), new Random().nextInt(maxSize)), new Random().nextInt(5) + 1);
        for (PointBean pointBean : snakeBody) {
            if (pointBean.getX() == foodBean.getPointBean().getX() && pointBean.getY() == foodBean.getPointBean().getY()) {
                foodBean = createFood(snakeBody);
                break;
            }
        }
        return foodBean;
    }

    private void stopGame() {
        isPlaying = false;
        if (handler != null) {
            handler.removeCallbacks(runnable);
            handler.removeCallbacksAndMessages(null);
        }
    }
}
