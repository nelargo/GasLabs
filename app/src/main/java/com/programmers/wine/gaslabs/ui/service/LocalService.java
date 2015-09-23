package com.programmers.wine.gaslabs.ui.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.Random;

public class LocalService extends Service {
    private final IBinder iBinder = new LocalBinder();
    private final Random random = new Random();

    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    public class LocalBinder extends Binder {
        LocalService getService() {
            return LocalService.this;
        }
    }

    /**
     * Public method to be access by the client
     *
     * @return Return a random number
     */
    public int getRandomNumber() {
        return random.nextInt(100);
    }
}
