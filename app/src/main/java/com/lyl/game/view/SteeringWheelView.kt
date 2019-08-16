package com.lyl.game.view

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.TimeInterpolator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.OvershootInterpolator
import com.lyl.game.R
import com.lyl.game.enums.GameDirection
import com.lyl.game.utils.DensityUtil
import java.lang.Math.*
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.sin

/**
 * 方向盘控件。
 */
class SteeringWheelView : View {
    /**
     * 外部监听器
     */
    private var mListener: SteeringWheelListener? = null
    /**
     * 画笔对象
     */
    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    /**
     * 画笔的颜色
     */
    private var mColor: Int = 0
    /**
     * 当前中心X
     */
    private var mCenterX: Float = 0.toFloat()
    /**
     * 当前中心Y
     */
    private var mCenterY: Float = 0.toFloat()
    /**
     * 球
     */
    private var mBallDrawable: Drawable? = null
    /**
     * 被按下后，球的图片
     */
    private var mBallPressedDrawable: Drawable? = null
    /**
     * 当前球中心X坐标
     */
    private var mBallCenterX: Float = 0.toFloat()
    /**
     * 当前球中心Y坐标
     */
    private var mBallCenterY: Float = 0.toFloat()
    /**
     * 球的半径
     */
    private var mBallRadius: Float = 0.toFloat()
    /**
     * 当前半径
     */
    private var mRadius: Float = 0.toFloat()
    /**
     * 当前角度
     */
    private var mAngle: Double = 0.toDouble()
    /**
     * 当前偏离中心的百分比，取值为 0 - 100
     */
    private var mPower: Int = 0
    /**
     * 通知的时间最小间隔
     */
    private var mNotifyInterval: Long = 0
    /**
     * 通知者
     */
    private var mNotifyRunnable: Runnable? = null
    /**
     * 上次通知监听者的时间
     */
    private var mLastNotifyTime: Long = 0
    /**
     * 当前方向
     */
    private var mDirection = GameDirection.INVALID
    /**
     * 向右箭头
     */
    private var mArrowRightDrawable: Drawable? = null
    /**
     * 回弹动画
     */
    private var mAnimator: ObjectAnimator? = null

    /**
     * 时间插值器
     */
    private var mInterpolator: TimeInterpolator? = null
    private var mWasTouched: Boolean = false

    /**
     * 获取球X坐标
     *
     * @return 球X坐标
     */
    /**
     * 设置球X坐标。目前该API的执行时机为Choreographer中每帧中的动画阶段,由底层动画框架反射调用
     *
     * @param ballX 球X坐标
     */
    var ballX: Float
        get() = mBallCenterX
        set(ballX) {
            if (ballX != mBallCenterX) {
                mBallCenterX = ballX
                updatePower()
                updateDirection()
                invalidate()
                notifyStatusChanged()
            }
        }

    /**
     * 获取球Y坐标
     *
     * @return 球Y坐标
     */
    /**
     * 设置球Y坐标。目前该API的执行时机为Choreographer中每帧中的动画阶段,由底层动画框架反射调用
     *
     * @param ballY 球Y坐标
     */
    var ballY: Float
        get() = mBallCenterY
        set(ballY) {
            if (mBallCenterY != ballY) {
                mBallCenterY = ballY
                updatePower()
                updateDirection()
                invalidate()
                notifyStatusChanged()
            }
        }

    private val interpolator: TimeInterpolator
        get() {
            if (mInterpolator == null) {
                mInterpolator = OvershootInterpolator()
            }
            return mInterpolator!!
        }

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.SteeringWheelView)
        //读取XML配置
        mColor = a.getColor(R.styleable.SteeringWheelView_ballColor, Color.RED)
        mArrowRightDrawable = a.getDrawable(R.styleable.SteeringWheelView_arrowRight)
        mBallDrawable = a.getDrawable(R.styleable.SteeringWheelView_ballSrc)
        mBallPressedDrawable = a.getDrawable(R.styleable.SteeringWheelView_ballPressedSrc)
        a.recycle()
        mBallRadius = (mBallDrawable!!.intrinsicWidth shr 1).toFloat()
        mDefaultWidth = DensityUtil.dip2px(mDefaultWidthDp.toFloat())
        mDefaultHeight = DensityUtil.dip2px(mDefaultHeightDp.toFloat())
        mPaint.color = mColor
        mPaint.style = Paint.Style.STROKE
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Log.d(TAG, "onMeasure: ")
        //handle wrap_content
        val widthSpecMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)
        //解析上层ViewGroup传下来的数据，高两位是模式，低30位是大小
        //主要需要特殊处理wrap_content情形
        val heightSpecMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSpecSize = MeasureSpec.getSize(heightMeasureSpec)

        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mDefaultWidth, mDefaultHeight)
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mDefaultWidth, heightSpecSize)
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, mDefaultHeight)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        //在layout过程中会回调该方法
        //handle padding
        val paddingLeft = paddingLeft
        val paddingRight = paddingRight
        val paddingTop = paddingTop
        val paddingBottom = paddingBottom

        val width = width - paddingLeft - paddingRight
        val height = height - paddingTop - paddingBottom

        val min = (min(width, height) shr 1)
        mRadius = (min - mArrowRightDrawable!!.intrinsicWidth / 2).toFloat()
        mCenterX = (paddingLeft + (width shr 1)).toFloat()
        mBallCenterX = mCenterX
        mCenterY = (paddingTop + (height shr 1)).toFloat()
        mBallCenterY = mCenterY

        //calc arrow bounds
        mArrowRightDrawable!!.setBounds((mCenterX + mRadius - mArrowRightDrawable!!.intrinsicWidth / 2).toInt(),
                (mCenterY - mArrowRightDrawable!!.intrinsicHeight / 2).toInt(),
                (mCenterX + mRadius + (mArrowRightDrawable!!.intrinsicWidth / 2).toFloat()).toInt(),
                (mCenterY + mArrowRightDrawable!!.intrinsicHeight / 2).toInt())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //画横线
        canvas.drawLine(mCenterX - mRadius, mCenterY, mCenterX + mRadius, mCenterY, mPaint)
        //画竖线
        canvas.drawLine(mCenterX, mCenterY - mRadius, mCenterX, mCenterY + mRadius, mPaint)
        //画大圆
        canvas.drawCircle(mCenterX, mCenterY, mRadius, mPaint)
        //画球
        drawBall(canvas)
        //画箭头
        drawArrow(canvas)
    }

    private fun drawBall(canvas: Canvas) {
        val drawable: Drawable? = if (mWasTouched) {
            mBallPressedDrawable
        } else {
            mBallDrawable
        }
        //point to the right drawable instance
        drawable!!.setBounds((mBallCenterX - drawable.intrinsicWidth / 2).toInt(),
                (mBallCenterY - drawable.intrinsicHeight / 2).toInt(),
                (mBallCenterX + drawable.intrinsicWidth / 2).toInt(),
                (mBallCenterY + drawable.intrinsicHeight / 2).toInt())
        drawable.draw(canvas)
    }

    /**
     * 画箭头
     *
     * @param canvas 画布对象
     */
    private fun drawArrow(canvas: Canvas) {
        if (!mWasTouched)
            return

        canvas.save()
        //旋转角度
        canvas.rotate((-mAngle).toFloat(), mCenterX, mCenterY)
        mArrowRightDrawable!!.draw(canvas)
        canvas.restore()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        Log.d(TAG, "onTouchEvent: ")
        val x = event.x.toInt()
        val y = event.y.toInt()
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mWasTouched = true
                if (mAnimator != null && mAnimator!!.isRunning) {
                    //在本次触摸事件序列中，如果上一个复位动画还没执行完毕，则需要取消动画，及时响应用户输入
                    mAnimator!!.cancel()
                }
            }
            MotionEvent.ACTION_MOVE -> {
                updateBallData(x, y)
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                mWasTouched = false
                resetBall()
            }
            else -> {
            }
        }

        notifyStatusChanged()
        return true
    }

    /**
     * 指定球回弹动画时间插值器
     *
     * @param value 插值器
     */
    fun interpolator(value: TimeInterpolator?): SteeringWheelView {
        if (value != null) {
            mInterpolator = value
        } else {
            mInterpolator = OvershootInterpolator()
        }
        return this
    }

    /**
     * 弹性滑动
     */
    private fun resetBall() {
        val pvhX = PropertyValuesHolder.ofFloat("BallX", mBallCenterX, mCenterX)
        val pvhY = PropertyValuesHolder.ofFloat("BallY", mBallCenterY, mCenterY)
        mAnimator = ObjectAnimator.ofPropertyValuesHolder(this, pvhX, pvhY).setDuration(150)
        mAnimator!!.interpolator = interpolator
        mAnimator!!.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {

            }

            override fun onAnimationEnd(animation: Animator) {
                mAngle = 0.0
                mPower = 0
                mDirection = GameDirection.INVALID
                notifyStatusChanged()
            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {

            }
        })
        mAnimator!!.start()
    }

    private fun updateBallData(x: Int, y: Int) {
        mBallCenterX = x.toFloat()
        mBallCenterY = y.toFloat()
        val outOfRange = outOfRange(x, y)
        //采用(a, b]开闭区间
        if (x >= mCenterX && y < mCenterY) {
            //第一象限
            mAngle = toDegrees(atan(((mCenterY - y) / (x - mCenterX)).toDouble()))
            if (outOfRange) {
                mBallCenterX = (mCenterX + cos(toRadians(mAngle)) * (mRadius - mBallRadius)).toFloat()
                mBallCenterY = (mCenterY - sin(toRadians(mAngle)) * (mRadius - mBallRadius)).toFloat()
            }
        } else if (x < mCenterX && y <= mCenterY) {
            //第二象限
            mAngle = 180 - toDegrees(atan(((mCenterY - y) / (mCenterX - x)).toDouble()))
            if (outOfRange) {
                mBallCenterX = (mCenterX - cos(toRadians(180 - mAngle)) * (mRadius - mBallRadius)).toFloat()
                mBallCenterY = (mCenterY - sin(toRadians(180 - mAngle)) * (mRadius - mBallRadius)).toFloat()
            }
        } else if (x <= mCenterX && y > mCenterY) {
            //第三象限
            mAngle = 270 - toDegrees(atan(((mCenterX - x) / (y - mCenterY)).toDouble()))
            if (outOfRange) {
                mBallCenterX = (mCenterX - cos(toRadians(mAngle - 180)) * (mRadius - mBallRadius)).toFloat()
                mBallCenterY = (mCenterY + sin(toRadians(mAngle - 180)) * (mRadius - mBallRadius)).toFloat()
            }
        } else if (x > mCenterX && y >= mCenterY) {
            //第四象限
            mAngle = 360 - toDegrees(atan(((y - mCenterY) / (x - mCenterX)).toDouble()))
            if (outOfRange) {
                mBallCenterX = (mCenterX + cos(toRadians(360 - mAngle)) * (mRadius - mBallRadius)).toFloat()
                mBallCenterY = (mCenterY + sin(toRadians(360 - mAngle)) * (mRadius - mBallRadius)).toFloat()
            }
        }
        updatePower()
        updateDirection()
        invalidate()
    }

    private fun updatePower() {
        val x = pow((mBallCenterX - mCenterX).toDouble(), 2.0)
        val y = pow((mBallCenterY - mCenterY).toDouble(), 2.0)
        val s = sqrt(x + y)
        mPower = (100 * s / (mRadius - mBallRadius)).toInt()
        Log.d(TAG, "updatePower: mPower = $mPower")
    }

    private fun outOfRange(newX: Int, newY: Int): Boolean {
        return pow((newX - mCenterX).toDouble(), 2.0) + pow((newY - mCenterY).toDouble(), 2.0) > pow((mRadius - mBallRadius).toDouble(), 2.0)
    }

    /**
     * 采用(a,b]开闭区间
     *
     * @return 方向值
     */
    private fun updateDirection(): GameDirection {
        mDirection = if (abs(mCenterX - mBallCenterX) < 0.00000001 && abs(mCenterY - mBallCenterY) < 0.00000001)
            GameDirection.INVALID
        else if (mAngle <= 45 || mAngle > 315)
            GameDirection.Right
        else if (mAngle > 45 && mAngle <= 135)
            GameDirection.Up
        else if (mAngle > 135 && mAngle <= 225)
            GameDirection.Left
        else
            GameDirection.Down
        return mDirection
    }

    /**
     * 通知监听者方向盘状态改变
     */
    private fun notifyStatusChanged() {
        if (mListener == null)
            return

        var delay: Long = 0
        if (mNotifyRunnable == null) {
            mNotifyRunnable = createNotifyRunnable()
        } else {
            val now = System.currentTimeMillis()
            if (now - mLastNotifyTime < mNotifyInterval) {
                //移除旧消息
                removeCallbacks(mNotifyRunnable)
                delay = mNotifyInterval - (now - mLastNotifyTime)
            }
        }

        postDelayed(mNotifyRunnable, delay)
    }

    private fun createNotifyRunnable(): Runnable {
        return Runnable {
            Log.d(TAG, "run: ")
            mLastNotifyTime = System.currentTimeMillis()
            //取当前数据，而非过去数据的snapshot
            mListener!!.onStatusChanged(this@SteeringWheelView, mAngle.toInt(), mPower, mDirection)
        }
    }

    /**
     * 设置回调时间间隔
     *
     * @param interval 回调时间间隔
     */
    fun notifyInterval(interval: Long): SteeringWheelView {
        if (interval < 0) {
            throw RuntimeException("notifyInterval interval < 0 is not accept")
        }

        mNotifyInterval = interval
        return this
    }

    /**
     * 设置监听器
     *
     * @param listener 监听器对象
     */
    fun listener(listener: SteeringWheelListener): SteeringWheelView {
        mListener = listener
        return this
    }

    interface SteeringWheelListener {
        /**
         * 方向盘状态改变的回调
         *
         * @param view      方向盘实例对象
         * @param angle     当前角度。范围0-360，其中右0，上90，左180，下270
         * @param power     方向上的力度。范围0-100
         * @param direction 大致方向。取值为 [.RIGHT] [.UP] [.LEFT] [.DOWN]
         */
        fun onStatusChanged(view: SteeringWheelView, angle: Int, power: Int, direction: GameDirection)
    }

    companion object {
        private const val TAG = "SteeringWheelView"
        private const val mDefaultWidthDp = 200
        private const val mDefaultHeightDp = 200
        /**
         * 当采用wrap_content测量模式时，默认宽度
         */
        private var mDefaultWidth: Int = 0
        /**
         * 当采用wrap_content测量模式时，默认高度
         */
        private var mDefaultHeight: Int = 0
    }
}
