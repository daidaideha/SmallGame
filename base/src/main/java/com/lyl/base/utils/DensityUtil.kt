package com.lyl.base.utils

import com.lyl.base.BaseApplication

object DensityUtil {

    private var density = -1f
    private var widthPixels = -1
    private var heightPixels = -1

    val screenWidth: Int
        get() {
            if (widthPixels <= 0) {
                widthPixels = BaseApplication.application.getResources().getDisplayMetrics().widthPixels
            }
            return widthPixels
        }


    val screenHeight: Int
        get() {
            if (heightPixels <= 0) {
                heightPixels = BaseApplication.application.getResources().getDisplayMetrics().heightPixels
            }
            return heightPixels
        }

    fun getDensity(): Float {
        if (density <= 0f) {
            density = BaseApplication.application.getResources().getDisplayMetrics().density
        }
        return density
    }

    fun dip2px(dpValue: Float): Int {
        return (dpValue * getDensity() + 0.5f).toInt()
    }

    fun px2dip(pxValue: Float): Int {
        return (pxValue / getDensity() + 0.5f).toInt()
    }
}
