package com.programmers.wine.gaslabs.ui.bluetooth;


import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.programmers.wine.gaslabs.R;
import com.programmers.wine.gaslabs.ui.bluetooth.connect.BluetoothLeService;
import com.programmers.wine.gaslabs.ui.bluetooth.connect.Tags;
import com.programmers.wine.gaslabs.ui.home.HomeActivity;
import com.programmers.wine.gaslabs.util.BaseFragment;
import com.programmers.wine.gaslabs.util.Utils;


public class DeviceFragment extends BaseFragment implements View.OnClickListener {
    protected static final int RES_TITLE = R.string.title_fragment_device;
    private BluetoothDevice bluetoothDevice;
    private boolean showToolbar;
    private Button btnDeviceControl;
    private ImageView icon;
    private TextView title;
    private TextView subtitle;
    private TextView connectionState;

    private boolean connected = false;
    private boolean disconnectedByUser = false;
    private BluetoothLeService service;

    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            service = ((BluetoothLeService.LocalBinder) iBinder).getService();
            if (!service.initialize()) {
                Toast.makeText(getActivity(), "Service can't be initialized, bluetooth problem", Toast.LENGTH_LONG).show();
                getActivity().finish();
            }

            if (bluetoothDevice != null) {
                service.connect(bluetoothDevice.getAddress());
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            service = null;
        }
    };

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            dismissDialog();
            String action = intent.getAction();
            if (Tags.ACTION_GATT_CONNECTED.equals(action)) {
                connected = true;
                updateConnectionState(R.string.device_state_connected);
                getActivity().invalidateOptionsMenu();
                Toast.makeText(getContext(), R.string.device_state_connected, Toast.LENGTH_SHORT).show();
            } else if (Tags.ACTION_GATT_DISCONNECTED.equals(action)) {
                connected = false;
                updateConnectionState(R.string.device_state_disconnected);
                Toast.makeText(getContext(), R.string.device_state_disconnected, Toast.LENGTH_SHORT).show();
                getActivity().invalidateOptionsMenu();
                if (!disconnectedByUser) {
                    showDialog();
                } else {
                    disconnectedByUser = false;
                }
            } else if (Tags.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // show gatt services.

            } else if (Tags.ACTION_DATA_AVAILABLE.equals(action)) {
                // show data
                Toast.makeText(getActivity(), intent.getExtras().getString(Tags.EXTRA_DATA), Toast.LENGTH_SHORT).show();
            }
        }
    };


    public static DeviceFragment newInstance(BluetoothDevice bluetoothDevice, boolean showToolbar) {
        DeviceFragment fragment = new DeviceFragment();
        fragment.bluetoothDevice = bluetoothDevice;
        fragment.showToolbar = showToolbar;
        return fragment;
    }

    public DeviceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_device, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (showToolbar) {
            setHasOptionsMenu(true);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // init views
        initToolbar(view);
        initViews(view);

        populateView(bluetoothDevice);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_device, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (showToolbar) {
                    if (getActivity() instanceof HomeActivity) {
                        ((HomeActivity) getActivity()).getDrawerLayout().openDrawer(GravityCompat.START);
                    } else if (getActivity() instanceof DeviceActivity) {
                        DeviceActivity deviceActivity = (DeviceActivity) getActivity();
                        deviceActivity.finish();
                    }
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if (view == btnDeviceControl) {
            if (!connected) {
                // start service
                showDialog();
                Intent gattServiceIntent = new Intent(getActivity(), BluetoothLeService.class);
                getActivity().bindService(gattServiceIntent, connection, Context.BIND_AUTO_CREATE);
            } else {
                disconnectedByUser = true;
                service.disconnect();
            }
        }
    }

    private void initToolbar(View root) {
        Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar);
        if (!showToolbar) {
            toolbar.setVisibility(View.GONE);
            return;
        }

        if (getActivity() instanceof AppCompatActivity) {
            AppCompatActivity appCompatActivity = ((AppCompatActivity) getActivity());
            appCompatActivity.setSupportActionBar(toolbar);
            ActionBar actionBar = appCompatActivity.getSupportActionBar();

            if (actionBar != null) {
                actionBar.setTitle(RES_TITLE);
                actionBar.setIcon(R.mipmap.ic_launcher);

                if (getActivity() instanceof HomeActivity) {
                    HomeActivity homeActivity = (HomeActivity) getActivity();
                    ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), homeActivity.getDrawerLayout(), toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
                    homeActivity.getDrawerLayout().setDrawerListener(actionBarDrawerToggle);
                    actionBarDrawerToggle.syncState();
                } else {
                    actionBar.setDisplayHomeAsUpEnabled(true);
                }
            }

        }
    }

    private void initViews(View root) {
        icon = (ImageView) root.findViewById(R.id.device_icon);
        title = (TextView) root.findViewById(R.id.device_title);
        subtitle = (TextView) root.findViewById(R.id.device_subtitle);
        connectionState = (TextView) root.findViewById(R.id.device_state);

        btnDeviceControl = (Button) root.findViewById(R.id.btn_device_control);

        btnDeviceControl.setOnClickListener(this);
    }

    private void populateView(BluetoothDevice bluetoothDevice) {
        if (bluetoothDevice != null) {
            title.setText(bluetoothDevice.getName());
            subtitle.setText(bluetoothDevice.getAddress());

            if (!isDeviceConnected()) {
                btnDeviceControl.setText(R.string.action_device_connect);
                connectionState.setText(R.string.device_state_disconnected);
                Utils.setColorButton(getContext(), btnDeviceControl, R.color.colorButtonPrimary, R.drawable.btn_primary);
            } else {
                btnDeviceControl.setText(R.string.action_device_connect);
                connectionState.setText(R.string.device_state_connected);
                Utils.setColorButton(getContext(), btnDeviceControl, R.color.colorButtonSecondary, R.drawable.btn_primary);
            }
        } else {
            // TODO: close this fragment because we don't have a bluetooth device to set it up
            Logger.d("No device to set it up");
        }
    }

    private boolean isDeviceConnected() {
        return false;
    }

    private void updateConnectionState(final int resourceId) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                connectionState.setText(resourceId);
                if (connected) {
                    Utils.setColorButton(getActivity(), btnDeviceControl, R.color.colorButtonSecondary, R.drawable.btn_primary);
                    btnDeviceControl.setText(R.string.action_device_disconnect);
                } else if (resourceId == R.string.device_state_disconnected) {
                    Utils.setColorButton(getActivity(), btnDeviceControl, R.color.colorButtonPrimary, R.drawable.btn_primary);
                    btnDeviceControl.setText(R.string.action_device_connect);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(receiver, makeGattUpdateIntentFilter());
        if (service != null) {
            if (bluetoothDevice != null) {
                boolean result = service.connect(bluetoothDevice.getAddress());
                Logger.d("Connect request result = " + result);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(receiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        service = null;
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Tags.ACTION_GATT_CONNECTED);
        intentFilter.addAction(Tags.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(Tags.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(Tags.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }
}
