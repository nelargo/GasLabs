package com.programmers.wine.gaslabs.ui.bluetooth.connect;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothProfile;
import android.content.Context;

import com.orhanobut.logger.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class XiaomiGattCallback extends BluetoothGattCallback implements XiaomiCallback {
    private BluetoothGatt bluetoothGatt;
    private Callback callback;
    private BluetoothDevice bluetoothDevice;

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        super.onConnectionStateChange(gatt, status, newState);
        if (newState == BluetoothProfile.STATE_CONNECTED) {
            Logger.d("STATE_CONNECTED");
            gatt.discoverServices();
            // pair
            pairDevice(bluetoothDevice);

            callback.onSuccess();
        } else {
            callback.onFailure();
        }

        if (newState == BluetoothProfile.STATE_DISCONNECTED) {
            Logger.d("STATE_DISCONNECTED");
        }
    }

    @Override
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        super.onCharacteristicRead(gatt, characteristic, status);
    }

    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        super.onCharacteristicWrite(gatt, characteristic, status);
    }

    @Override
    public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
        super.onReadRemoteRssi(gatt, rssi, status);
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        super.onServicesDiscovered(gatt, status);
        if (status == BluetoothGatt.GATT_SUCCESS) {
            Logger.d("Services Discovered: GATT_SUCCESS");
            this.bluetoothGatt = gatt;
            // success
        } else {
            // failure
        }
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        super.onCharacteristicChanged(gatt, characteristic);
    }



    /*
    * Xiaomi callbacks.
    */

    @Override
    public void connect(Context context, String address, Callback callback) {
        this.callback = callback;
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(address);
        this.bluetoothDevice = bluetoothDevice;
        bluetoothDevice.connectGatt(context, true, this);
        Logger.d("Device: " + bluetoothDevice.getAddress() + "\nBound state: " + bluetoothDevice.getBondState());
    }

    @Override
    public void disconnect() {

    }

    @Override
    public boolean isDeviceConnected() {
        return false;
    }

    private boolean pairDevice(BluetoothDevice device) {
        Logger.d("Try pair device: " + device.getAddress());
        try {

            Method method = device.getClass().getMethod("createBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
            return true;
        } catch (NoSuchMethodException e) {
            Logger.e(e.getMessage());
            return false;
        } catch (InvocationTargetException e) {
            Logger.e(e.getMessage());
            return false;
        } catch (IllegalAccessException e) {
            Logger.e(e.getMessage());
            return false;
        }
    }
}
