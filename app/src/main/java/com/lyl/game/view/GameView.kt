package com.lyl.game.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View

import com.lyl.game.enums.GameDirection
import com.lyl.game.enums.GameOperate
import com.lyl.game.interfaces.IGameAction
import com.lyl.game.interfaces.IGameBody
import com.lyl.game.utils.DensityUtil
import kotlin.math.min

/**
 * create lyl on 2019-08-13
 *
 * 游戏视图
 */
class GameView : View, IGameAction {

    private var gameBody: IGameBody? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun setGameBody(gameBody: IGameBody) {
        this.gameBody = gameBody
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (gameBody != null) gameBody!!.drawGames(canvas)
    }

    override fun destroy() {
        if (gameBody != null) gameBody!!.destroy()
    }

    override fun actionDirection(direction: GameDirection) {
        if (gameBody != null) gameBody!!.actionDirection(direction)
    }

    override fun actionOperate(operate: GameOperate) {
        if (gameBody != null) gameBody!!.actionOperate(operate)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val mWidth = measureDimension(DensityUtil.screenWidth, widthMeasureSpec)
        val mHeight = measureDimension(DensityUtil.screenWidth, heightMeasureSpec)
        setMeasuredDimension(mWidth, mHeight)
    }

    /***
     * 计算控件宽高
     *
     * @param defaultSize 默认大小
     * @param measureSpec
     * @return 最终大小
     */
    private fun measureDimension(defaultSize: Int, measureSpec: Int): Int {
        val result: Int
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        result = when (specMode) {
            MeasureSpec.EXACTLY -> specSize
            MeasureSpec.AT_MOST -> min(defaultSize, specSize)
            else -> defaultSize
        }
        return result
    }
}
