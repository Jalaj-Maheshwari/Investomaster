package com.android.App_25;

import android.app.Application;

import timber.log.Timber;

public class InvestoMaster extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.uprootAll();
            Timber.plant(new Timber.DebugTree());
        }
    }
}
