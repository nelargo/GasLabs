package com.programmers.wine.gaslabs.ui.bluetooth;


import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.programmers.wine.gaslabs.R;

import java.util.ArrayList;
import java.util.List;

public class ScanDeviceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<BluetoothDevice> bluetoothDeviceList;
    private Context context;

    public ScanDeviceAdapter() {
        bluetoothDeviceList = new ArrayList<>();
    }

    @Override
    public int getItemCount() {
        if (bluetoothDeviceList != null) {
            return bluetoothDeviceList.size();
        } else {
            return 0;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_device, parent, false);
        context = parent.getContext();
        return new DeviceVH(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BluetoothDevice bluetoothDevice = bluetoothDeviceList.get(position);
        if (holder instanceof DeviceVH) {
            DeviceVH deviceVH = (DeviceVH) holder;
            deviceVH.populateView(context,bluetoothDevice);
        }
    }

    /**
     * Add device to the list of devices.
     *
     * @param bluetoothDevice Device to be add.
     */
    protected void addDevice(BluetoothDevice bluetoothDevice) {
        if (bluetoothDeviceList != null && !bluetoothDeviceList.contains(bluetoothDevice)) {
            bluetoothDeviceList.add(bluetoothDevice);
        }
    }

    /**
     * Clear list of bluetooth devices.
     */
    protected void clear() {
        bluetoothDeviceList.clear();
    }

    protected BluetoothDevice getDevice(int position) {
        if (bluetoothDeviceList.size() > position) {
            return bluetoothDeviceList.get(position);
        } else {
            return null;
        }
    }
}
