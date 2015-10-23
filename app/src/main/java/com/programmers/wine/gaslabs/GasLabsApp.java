package com.programmers.wine.gaslabs;

import android.app.Application;
import android.content.Intent;

import com.orhanobut.logger.Logger;
import com.programmers.wine.gaslabs.ui.bluetooth.service.XiaomiService;

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

        // start service
        Intent intentService = new Intent(getBaseContext(), XiaomiService.class);
        getBaseContext().startService(intentService);
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
                //.setMethodCount(0)
                .hideThreadInfo();
    }
}
