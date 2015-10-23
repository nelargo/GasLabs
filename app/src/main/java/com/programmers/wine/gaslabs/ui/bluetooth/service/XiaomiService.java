package com.programmers.wine.gaslabs.ui.bluetooth.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;

import com.programmers.wine.gaslabs.ui.bluetooth.connect.Callback;
import com.programmers.wine.gaslabs.ui.bluetooth.connect.Tags;
import com.programmers.wine.gaslabs.ui.bluetooth.connect.XiaomiGattCallback;

public class XiaomiService extends Service {
    private Messenger messenger = new Messenger(new IncomingHandler());
    private XiaomiGattCallback xiaomiGattCallback;

    public XiaomiService() {
    }

    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case MSG.MSG_CONNECT:
                    String address = (String) msg.obj;
                    connect(address);
                    break;

                case MSG.MSG_DISCONNECT:
                    break;

                case MSG.MSG_CONNECTING:
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        xiaomiGattCallback = new XiaomiGattCallback();
        return START_STICKY;
    }

    private void connect(String address) {
        xiaomiGattCallback.connect(getBaseContext(), address, new Callback() {
            @Override
            public void onSuccess() {
                sendBroadcast(new Intent(Tags.ACTION_GATT_CONNECTED));
            }

            @Override
            public void onFailure() {
                sendBroadcast(new Intent(Tags.ACTION_GATT_DISCONNECTED));
            }
        });
    }
}
