package com.lyl.game

import android.content.Context
import androidx.multidex.MultiDex
import com.alibaba.android.arouter.launcher.ARouter
import com.lyl.base.BaseApplication

/**
 * create lyl on 2019-08-13
 *
 */
class GameApplication : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {

        lateinit var instance: GameApplication
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}
