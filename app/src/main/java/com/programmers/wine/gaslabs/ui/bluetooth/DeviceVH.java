package com.programmers.wine.gaslabs.ui.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.programmers.wine.gaslabs.R;
import com.programmers.wine.gaslabs.util.Tags;


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

    protected void populateView(Context context, BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
        title.setText(bluetoothDevice.getName());
        subtitle.setText(bluetoothDevice.getAddress());
        subtitle.setVisibility(View.VISIBLE);
        if (bluetoothDevice.getAddress().toLowerCase().contains(Tags.XIAOMI_MI_BAND_START_ADDRESS)) {
            icon.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.ic_xiaomi));
        } else {
            icon.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.ic_launcher));
        }
    }

    public BluetoothDevice getBluetoothDevice() {
        return bluetoothDevice;
    }

}
