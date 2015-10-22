package com.programmers.wine.gaslabs.ui.bluetooth.connect;

import android.content.Context;


public interface XiaomiCallback {
    void connect(Context context, String address, Callback callback);

    void disconnect();

    boolean isDeviceConnected();
}
