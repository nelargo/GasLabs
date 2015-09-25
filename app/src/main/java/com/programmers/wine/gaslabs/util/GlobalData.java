package com.programmers.wine.gaslabs.util;

import android.bluetooth.BluetoothDevice;

public class GlobalData {
    protected static final boolean debugMode = true;
    // protected static final boolean productionServer = false;
    protected static GlobalData globalData;
    protected BluetoothDevice selectedBluetoothDevice;

    private GlobalData() {
    }

    public static GlobalData getInstance() {
        if (globalData == null) {
            globalData = new GlobalData();
        }
        return globalData;
    }

    public static boolean isOnDebugMode() {
        return debugMode;
    }

    /*
    public static boolean isOnProductionServer() {
        return productionServer;
    }
    */

    public BluetoothDevice getSelectedBluetoothDevice() {
        return selectedBluetoothDevice;
    }

    public void setSelectedBluetoothDevice(BluetoothDevice selectedBluetoothDevice) {
        this.selectedBluetoothDevice = selectedBluetoothDevice;
    }
}
