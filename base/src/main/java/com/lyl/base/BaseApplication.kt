package com.lyl.base

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter

/**
 * create lyl on 2019-08-28
 * </p>
 */
open class BaseApplication : Application() {

    companion object {
        lateinit var application: BaseApplication
    }

    override fun onCreate() {
        super.onCreate()
        application = this

        if (BuildConfig.DEBUG) {
            ARouter.openLog()     // 打印日志
            ARouter.openDebug()   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this)
    }
}