package com.programmers.wine.gaslabs;

import android.app.Application;

import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class GasLabsApp extends Application {
    protected static final String TAG_APP = "GasLabsAPP";

    @Override
    public void onCreate() {
        super.onCreate();

        // Default font
        initCalligraphy();

        // Default logger
        initLogger();
    }

    protected void initCalligraphy() {
        CalligraphyConfig
                .initDefault(new CalligraphyConfig
                        .Builder()
                        .setDefaultFontPath("fonts/Raleway-Regular.ttf")
                        .setFontAttrId(R.attr.fontPath).build());
    }

    protected void initLogger() {
        Logger
                .init(TAG_APP)
                        // .hideThreadInfo()
                .setLogLevel(LogLevel.FULL);
    }
}
