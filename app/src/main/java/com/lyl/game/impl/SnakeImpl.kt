package com.lyl.game.impl

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Handler
import com.lyl.game.bean.FoodBean
import com.lyl.game.bean.PointBean
import com.lyl.game.enums.GameDirection
import com.lyl.game.enums.GameOperate
import com.lyl.game.interfaces.IGameBody
import com.lyl.game.interfaces.ISnakeCallback
import com.lyl.game.utils.DensityUtil
import java.util.*

/**
 * create lyl on 2019-08-13
 *
 * 贪吃蛇实现类
 */
class SnakeImpl(private var callback: ISnakeCallback?) : IGameBody {

    /**
     * 画笔
     */
    private val mSnakePaint: Paint
    private val mFoodPaint: Paint
    private val mBackgroundPaint: Paint
    /**
     * 动画handler
     */
    private var handler: Handler? = null
    /**
     * 动画runnable
     */
    private lateinit var runnable: Runnable

    /**
     * 蛇体数据
     */
    private val snakeBody = ArrayList<PointBean>()
    /**
     * 食物数据
     */
    private var foodBean: FoodBean? = null
    /**
     * 运动方向
     */
    private var direction = GameDirection.Right
    /**
     * 单格宽度
     */
    private val rowWidth: Float
    /**
     * 最大格数
     */
    private var maxSize: Int = 0
    /**
     * 屏幕宽度
     */
    private val screenSize: Int = DensityUtil.screenWidth
    /**
     * 蛇体宽度
     */
    private val snakeWidth: Int
    /**
     * 食物宽度
     */
    private val foodWidth: Int
    /**
     * 当前速度
     */
    private var speed = 500
    /**
     * 当前得分
     */
    private var score = 0
    /**
     * 正在游戏中
     */
    private var isPlaying: Boolean = false

    init {
        maxSize = when {
            screenSize <= 720 -> 20
            screenSize <= 1080 -> 40
            else -> 60
        }
        rowWidth = screenSize / (maxSize * 1.0f)
        snakeWidth = DensityUtil.dip2px(8f)
        foodWidth = DensityUtil.dip2px(6f)

        mFoodPaint = Paint()
        mFoodPaint.isAntiAlias = true
        mFoodPaint.color = Color.BLUE

        mSnakePaint = Paint()
        mSnakePaint.isAntiAlias = true
        mSnakePaint.color = Color.BLACK

        mBackgroundPaint = Paint()
        mBackgroundPaint.isAntiAlias = true
        mBackgroundPaint.color = Color.GRAY

        runnable = Runnable {
            if (this@SnakeImpl.callback != null && isPlaying) {
                this@SnakeImpl.callback!!.reDraw()
                val eatFood = isHit(snakeBody[0], foodBean!!.pointBean, direction)
                updateSnake(snakeBody, direction, eatFood)
                if (eatFood) foodBean = createFood(snakeBody)
                if (handler == null) return@Runnable
                handler!!.postDelayed(runnable, (if (speed <= 20) 20 else speed).toLong())
            }
        }
    }

    override fun drawGames(canvas: Canvas) {
        drawBackground(canvas)
        drawFood(canvas)
        drawSnake(canvas)
    }

    override fun destroy() {
        stopGame()
        clearOldData()
        callback = null
    }

    override fun actionDirection(direction: GameDirection) {
        if (!isPlaying || this.direction == direction || snakeBody.isEmpty() || direction == GameDirection.INVALID)
            return
        if (!isHit(snakeBody[0], snakeBody[1], direction)) {
            this.direction = direction
        }
    }

    override fun actionOperate(operate: GameOperate) {
        when (operate) {
            GameOperate.Start -> startGame()
            GameOperate.A -> {
                if (!isPlaying) return
                handler!!.removeCallbacks(runnable)
                handler!!.removeCallbacksAndMessages(null)
                handler!!.post(runnable)
            }
            else -> {
            }
        }
    }

    private fun startGame() {
        if (isPlaying) return
        clearOldData()
        isPlaying = true
        handler = Handler()
        this.score = 0
        this.speed = 500
        val header = Random().nextInt(maxSize - 4) + 2
        for (i in 0..2) {
            snakeBody.add(PointBean(header - i, header))
        }
        foodBean = createFood(snakeBody)
        runnable.run()
    }

    private fun clearOldData() {
        snakeBody.clear()
        foodBean = null
    }

    private fun drawBackground(canvas: Canvas) {
        canvas.drawRect(0f, 0f, screenSize.toFloat(), screenSize.toFloat(), mBackgroundPaint)
        for (i in 0 until maxSize) {
            canvas.drawLine(0f, i * rowWidth, screenSize.toFloat(), i * rowWidth, mSnakePaint)
            canvas.drawLine(i * rowWidth, 0f, i * rowWidth, screenSize.toFloat(), mSnakePaint)
        }
    }

    private fun drawFood(canvas: Canvas) {
        if (foodBean != null) {
            val halfWidth = foodWidth / 2.0f
            val x = foodBean!!.pointBean.x * rowWidth + rowWidth / 2
            val y = foodBean!!.pointBean.y * rowWidth + rowWidth / 2
            canvas.drawCircle(x, y, halfWidth, mFoodPaint)
        }
    }

    private fun drawSnake(canvas: Canvas) {
        if (snakeBody.size > 0) {
            for (i in snakeBody.indices) {
                val halfWidth = snakeWidth / 2.0f
                val x = snakeBody[i].x * rowWidth + rowWidth / 2
                val y = snakeBody[i].y * rowWidth + rowWidth / 2
                if (i == 0)
                    canvas.drawRect(x - halfWidth, y - halfWidth,
                            x + halfWidth, y + halfWidth, mSnakePaint)
                else
                    canvas.drawCircle(x, y, halfWidth, mSnakePaint)
            }
        }
    }

    private fun isHit(headerBean: PointBean, hitBean: PointBean, direction: GameDirection): Boolean {
        var x = headerBean.x
        var y = headerBean.y
        when (direction) {
            GameDirection.Left -> x--
            GameDirection.Up -> y--
            GameDirection.Right -> x++
            GameDirection.Down -> y++
            else -> {
            }
        }
        return x == hitBean.x && y == hitBean.y
    }

    private fun isOutBorderLine(x: Int, y: Int): Boolean {
        return x < 0 || y < 0 || x >= maxSize || y >= maxSize
    }

    private fun isHitBody(snake: List<PointBean>, x: Int, y: Int): Boolean {
        var isHit = false
        for (bean in snake) {
            if (bean.x == x && bean.y == y) {
                isHit = true
                break
            }
        }
        return isHit
    }

    private fun updateSnake(snake: MutableList<PointBean>, direction: GameDirection, eatFood: Boolean) {
        val lastIndex = snake.size - 1
        val first = snake[0]
        val second = snake[1]
        val last = snake[lastIndex]
        if (isHit(first, second, direction)) return
        var x = first.x
        var y = first.y
        when (direction) {
            GameDirection.Left -> x--
            GameDirection.Up -> y--
            GameDirection.Right -> x++
            GameDirection.Down -> y++
            else -> {
            }
        }
        if (isOutBorderLine(x, y) || isHitBody(snake, x, y)) {
            if (callback != null) callback!!.outBorderLine()
            stopGame()
            return
        }
        snake.removeAt(lastIndex)
        snake.add(0, PointBean(x, y))
        if (eatFood) {
            snake.add(last)
            score += foodBean!!.score
            speed = 500 - score / 2 * 20
            if (callback != null) callback!!.score(score)
        }
    }

    private fun createFood(snakeBody: List<PointBean>): FoodBean {
        var foodBean = FoodBean(PointBean(Random().nextInt(maxSize), Random().nextInt(maxSize)), Random().nextInt(5) + 1)
        for (pointBean in snakeBody) {
            if (pointBean.x == foodBean.pointBean.x && pointBean.y == foodBean.pointBean.y) {
                foodBean = createFood(snakeBody)
                break
            }
        }
        return foodBean
    }

    private fun stopGame() {
        isPlaying = false
        if (handler != null) {
            handler!!.removeCallbacks(runnable)
            handler!!.removeCallbacksAndMessages(null)
            handler = null
        }
    }
}
