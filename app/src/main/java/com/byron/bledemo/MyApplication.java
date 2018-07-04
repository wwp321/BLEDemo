package com.byron.bledemo;

import android.app.Application;
import android.content.Context;

import com.byron.bledemo.DebugTree.MiPhoneDebugTree;

import timber.log.Timber;

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        Timber.plant(new MiPhoneDebugTree());
    }

    public static Context getContext() {
        return context;
    }
}
