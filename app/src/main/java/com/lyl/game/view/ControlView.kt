package com.lyl.game.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.OvershootInterpolator
import android.widget.Button
import android.widget.FrameLayout
import com.lyl.game.R

/**
 * create lyl on 2019-08-16
 *
 */
class ControlView : FrameLayout {

    private var wheelView: SteeringWheelView? = null
    private var btnStart: Button? = null
    private var btnSelect: Button? = null
    private var btnA: Button? = null
    private var btnB: Button? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        val view = LayoutInflater.from(context).inflate(R.layout.view_control, this, false)
        wheelView = view.findViewById(R.id.steeringWheelView)
        btnStart = view.findViewById(R.id.btnStart)
        btnSelect = view.findViewById(R.id.btnSelect)
        btnA = view.findViewById(R.id.btnA)
        btnB = view.findViewById(R.id.btnB)
        addView(view)
    }

    fun setDirectionListener(listener: SteeringWheelView.SteeringWheelListener) {
        wheelView!!.notifyInterval(16).listener(listener).interpolator(OvershootInterpolator())
    }

    fun setActionListener(onClickListener: OnClickListener) {
        btnStart!!.setOnClickListener(onClickListener)
        btnSelect!!.setOnClickListener(onClickListener)
        btnA!!.setOnClickListener(onClickListener)
        btnB!!.setOnClickListener(onClickListener)
    }
}
