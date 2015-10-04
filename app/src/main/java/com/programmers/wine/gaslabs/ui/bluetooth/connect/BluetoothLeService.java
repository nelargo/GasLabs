package com.programmers.wine.gaslabs.ui.bluetooth.connect;


import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;

import com.orhanobut.logger.Logger;

public class BluetoothLeService extends Service {
    private static final int STATE_CONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_DISCONNECTED = 2;

    private BluetoothManager manager;
    private BluetoothAdapter adapter;
    private BluetoothGatt gatt;

    private BluetoothDevice device;
    private String address;

    private final IBinder binder = new LocalBinder();

    /**
     * It has at all the times the status of the connection to the device.
     */
    private int connectionState = STATE_DISCONNECTED;


    /**
     * Class that provides to the client direct access to public methods in the BluetoothLeService.
     */
    public class LocalBinder extends Binder {

        /**
         * Getter of the BluetoothLeService instance.
         *
         * @return Instance of BluetoothLeService so clients can call public methods.
         */
        public BluetoothLeService getService() {
            return BluetoothLeService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        closeGatt();
        super.unbindService(conn);
    }

    public void closeGatt() {
        if (gatt == null) {
            return;
        }
        gatt.close();
        gatt = null;
    }

    public boolean initialize() {
        if (manager == null) {
            manager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
            if (manager == null) {
                return false;
            }
        }

        adapter = manager.getAdapter();
        if (adapter == null) {
            return false;
        }

        return true;
    }

    public boolean connect(final String address) {
        if (adapter == null || address == null) {
            return false;
        }

        // Try to reconnect
        if (reconnect(address)) {
            return true;
        }

        // Connect
        BluetoothDevice device = adapter.getRemoteDevice(address);
        if (device == null) {
            return false;
        }

        this.device = device;
        this.address = address;
        this.connectionState = STATE_CONNECTING;
        gatt = device.connectGatt(this, true, gattCallback);

        return true;
    }

    /*public boolean connect(BluetoothDevice device) {
        if (device == null) {
            return false;
        }

        if (device.equals(this.device) && gatt != null) {
            if (gatt.connect()) {
                return true;
            } else {
                return false;
            }
        }

        gatt = device.connectGatt(this, false, gattCallback);

        return false;
    }*/

    /**
     * Try to reconnect to device.
     *
     * @param address The address of the device that you want to reconnect.
     * @return True if can reconnect.
     */
    private boolean reconnect(String address) {
        if (this.address != null && address.equals(this.address) && gatt != null) {
            if (gatt.connect()) {
                connectionState = STATE_CONNECTING;
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void disconnect() {
        if (adapter == null || gatt == null) {
            return;
        }
        gatt.disconnect();
    }


    /**
     * GattCallback instance with what we want to do when the connection to the GATT Server happens.
     */
    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            // super.onConnectionStateChange(gatt, status, newState);
            handleNewState(newState);
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            //super.onServicesDiscovered(gatt, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(Tags.ACTION_GATT_SERVICES_DISCOVERED);
            } else {
                Logger.d("onServicesDiscovered received: " + status);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            // super.onCharacteristicRead(gatt, characteristic, status);
            broadcastUpdate(Tags.ACTION_DATA_AVAILABLE, characteristic);
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            // super.onCharacteristicChanged(gatt, characteristic);
            broadcastUpdate(Tags.ACTION_DATA_AVAILABLE, characteristic);
        }
    };

    private void handleNewState(int newState) {
        String intentAction;
        if (newState == BluetoothProfile.STATE_CONNECTED) {
            intentAction = Tags.ACTION_GATT_CONNECTED;
            connectionState = STATE_CONNECTED;

            // send msg
            broadcastUpdate(intentAction);

            // discover services
            Logger.d("Discover services: " + String.valueOf(this.gatt.discoverServices()));
        } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
            intentAction = Tags.ACTION_GATT_DISCONNECTED;
            connectionState = STATE_DISCONNECTED;

            // send msg
            broadcastUpdate(intentAction);
        }

        // Other state case are connecting and disconnecting.
    }

    private void broadcastUpdate(String action) {
        Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    private void broadcastUpdate(String action, BluetoothGattCharacteristic characteristic) {
        Intent intent = new Intent(action);
        final byte[] data = characteristic.getValue();
        if (data != null && data.length > 0) {
            StringBuilder stringBuilder = new StringBuilder(data.length);
            for (byte b : data) {
                // Convert byte to hexadecimal
                stringBuilder.append(String.format("%02X", b));
            }
            intent.putExtra(Tags.EXTRA_DATA, new String(data) + "\n" + stringBuilder.toString());
        }
        sendBroadcast(intent);
    }
}
