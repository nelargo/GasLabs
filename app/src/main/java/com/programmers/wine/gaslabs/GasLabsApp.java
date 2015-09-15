package com.programmers.wine.gaslabs;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class GasLabsApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Default font
        initCalligraphy();
    }

    protected void initCalligraphy() {
        CalligraphyConfig
                .initDefault(new CalligraphyConfig
                        .Builder()
                        .setDefaultFontPath("fonts/Raleway-Regular.ttf")
                        .setFontAttrId(R.attr.fontPath).build());
    }
}
