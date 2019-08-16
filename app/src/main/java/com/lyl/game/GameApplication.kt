package com.lyl.game

import android.app.Application

/**
 * create lyl on 2019-08-13
 *
 */
class GameApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {

        lateinit var instance: GameApplication
    }
}
