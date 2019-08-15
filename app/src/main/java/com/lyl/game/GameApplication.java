package com.lyl.game;

import android.app.Application;

/**
 * create lyl on 2019-08-13
 * </p>
 */
public class GameApplication extends Application {

    private static GameApplication instance;

    public static GameApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
