package com.dyrwi.lasttimesince.activities;

import android.app.Application;
import android.content.Context;

import net.danlew.android.joda.JodaTimeAndroid;

/**
 * Created by Ben on 09-Mar-16.
 */
public class MyApplication extends Application {
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }
}
