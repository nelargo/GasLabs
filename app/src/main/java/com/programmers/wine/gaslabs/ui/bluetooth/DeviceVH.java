package com.programmers.wine.gaslabs.ui.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.programmers.wine.gaslabs.R;


public class DeviceVH extends RecyclerView.ViewHolder {
    // protected TextView titleExternal;
    protected TextView title;
    protected TextView subtitle;
    protected ImageView icon;
    protected BluetoothDevice bluetoothDevice;

    public DeviceVH(View itemView) {
        super(itemView);
        // titleExternal = (TextView) itemView.findViewById(R.id.card_device_title_external);
        title = (TextView) itemView.findViewById(R.id.card_device_title);
        subtitle = (TextView) itemView.findViewById(R.id.card_device_subtitle);
        icon = (ImageView) itemView.findViewById(R.id.card_device_icon);
    }

    protected void populateView(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
        title.setText(bluetoothDevice.getName());
        subtitle.setText(bluetoothDevice.getAddress());
        subtitle.setVisibility(View.VISIBLE);
    }

    public BluetoothDevice getBluetoothDevice() {
        return bluetoothDevice;
    }

}
