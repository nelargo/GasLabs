package com.programmers.wine.gaslabs.ui.service;


import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;

import com.orhanobut.logger.Logger;

public class GasLabsService extends Service {
    private static final String ARG_SERVICES_START = "ArgServiceStart";
    private ServiceHandler serviceHandler;
    private Looper looper;

    /**
     * Handler that receives messages from the thread.
     */
    private class ServiceHandler extends Handler {

        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            long endTime = System.currentTimeMillis() + 5000;
            while (System.currentTimeMillis() < endTime) {
                synchronized (this) {
                    try {
                        wait(endTime - System.currentTimeMillis());
                    } catch (Exception e) {
                        Logger.e(e.getMessage());
                    }
                }
            }
            stopSelf(msg.arg1);
        }
    }


    public GasLabsService() {
    }

    @Override
    public void onCreate() {
        // This method was called only when the service is crated. If the service already running in this method is not called.
        Logger.d("Hi potato");

        HandlerThread handlerThread = new HandlerThread(ARG_SERVICES_START, Process.THREAD_PRIORITY_BACKGROUND);
        handlerThread.start();
        looper = handlerThread.getLooper();
        serviceHandler = new ServiceHandler(looper);
    }

    // The systems calls this method when another component wants to bind with the service by calling bindService()
    @Override
    public IBinder onBind(Intent intent) {
        Logger.d("on bind the potato");
        // We must to provide an interface that clients use to communicate with the service. No return null.
        return null;
    }

    // One this method executes, the service is started and can run in the background indefinitely
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.d("on start command potato");

        Message msg = serviceHandler.obtainMessage();
        msg.arg1 = startId;
        serviceHandler.sendMessage(msg);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Logger.d("Die potato");
    }

}
