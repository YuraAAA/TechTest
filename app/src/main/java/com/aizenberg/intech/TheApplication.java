package com.aizenberg.intech;

import android.app.Application;

/**
 * Created by Yuriy Aizenberg
 */
public class TheApplication extends Application {

    private static TheApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static TheApplication getInstance() {
        return instance;
    }

}
