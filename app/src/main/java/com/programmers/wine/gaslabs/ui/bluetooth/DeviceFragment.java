package com.programmers.wine.gaslabs.ui.bluetooth;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.programmers.wine.gaslabs.R;
import com.programmers.wine.gaslabs.ui.bluetooth.connect.Callback;
import com.programmers.wine.gaslabs.ui.bluetooth.connect.XiaomiGattCallback;
import com.programmers.wine.gaslabs.ui.home.HomeActivity;
import com.programmers.wine.gaslabs.util.BaseFragment;
import com.programmers.wine.gaslabs.util.GlobalData;
import com.programmers.wine.gaslabs.util.Tags;


public class DeviceFragment extends BaseFragment {
    private BluetoothDevice bluetoothDevice;
    private boolean showToolbar;
    private ImageView icon;
    private TextView title;
    private TextView subtitle;
    private TextView connectionState;

    private boolean connected = false;

    private final BroadcastReceiver mPairReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Logger.d("BroadcastReceiver \\(>_<)/");

            String action = intent.getAction();

            if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                final int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
                final int prevState = intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, BluetoothDevice.ERROR);

                if (state == BluetoothDevice.BOND_BONDED && prevState == BluetoothDevice.BOND_BONDING) {
                    Toast.makeText(getContext(), "Paired", Toast.LENGTH_SHORT).show();
                } else if (state == BluetoothDevice.BOND_NONE && prevState == BluetoothDevice.BOND_BONDED) {
                    Toast.makeText(getContext(), "Unpaired", Toast.LENGTH_SHORT).show();
                }
            }

            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_CONNECTED:
                        Toast.makeText(context,
                                "BTStateChangedBroadcastReceiver: STATE_CONNECTED",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case BluetoothAdapter.STATE_CONNECTING:
                        Toast.makeText(context,
                                "BTStateChangedBroadcastReceiver: STATE_CONNECTING",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case BluetoothAdapter.STATE_DISCONNECTED:
                        Toast.makeText(context,
                                "BTStateChangedBroadcastReceiver: STATE_DISCONNECTED",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case BluetoothAdapter.STATE_DISCONNECTING:
                        Toast.makeText(context,
                                "BTStateChangedBroadcastReceiver: STATE_DISCONNECTING",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case BluetoothAdapter.STATE_OFF:
                        Toast.makeText(context,
                                "BTStateChangedBroadcastReceiver: STATE_OFF",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Toast.makeText(context,
                                "BTStateChangedBroadcastReceiver: STATE_ON",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Toast.makeText(context,
                                "BTStateChangedBroadcastReceiver: STATE_TURNING_OFF",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Toast.makeText(context,
                                "BTStateChangedBroadcastReceiver: STATE_TURNING_ON",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
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
        if (showToolbar) {
            setHasOptionsMenu(true);
        }
        return inflater.inflate(R.layout.fragment_device, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // init views
        initToolbar(view);
        initViews(view);

        if (bluetoothDevice != null) {
            populateView(bluetoothDevice);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_device, menu);
        updateMenu(menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        updateMenu(menu);
        super.onPrepareOptionsMenu(menu);
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

            case R.id.action_device_connect:
                if (isBluetoothEnabled()) {
                    XiaomiGattCallback xiaomiGattCallback = new XiaomiGattCallback();
                    xiaomiGattCallback.connect(getContext(), bluetoothDevice.getAddress(), new Callback() {
                        @Override
                        public void onSuccess() {
                            updateConnectionState(true);
                        }

                        @Override
                        public void onFailure() {
                            updateConnectionState(false);
                        }
                    });
                } else {
                    Toast.makeText(getContext(), R.string.feedback_bluetooth_enabled_required, Toast.LENGTH_LONG).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void updateMenu(Menu menu) {
        MenuItem itemConnect = menu.findItem(R.id.action_device_connect);
        MenuItem itemDisconnect = menu.findItem(R.id.action_device_disconnect);

        if (isConnected()) {
            itemConnect.setVisible(false);
            itemDisconnect.setVisible(true);
        } else {
            itemConnect.setVisible(true);
            itemDisconnect.setVisible(false);
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
                if (bluetoothDevice != null) {
                    actionBar.setTitle(getString(R.string.device_detail) + " (" + bluetoothDevice.getName() + ")");
                } else {
                    actionBar.setTitle(R.string.title_fragment_device);
                    actionBar.setIcon(R.mipmap.ic_launcher);
                }


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
    }

    private void populateView(BluetoothDevice bluetoothDevice) {
        if (bluetoothDevice.getAddress().toLowerCase().contains(Tags.XIAOMI_MI_BAND_START_ADDRESS)) {
            icon.setImageResource(R.mipmap.ic_xiaomi);
        } else {
            icon.setImageResource(R.mipmap.ic_launcher);
        }
        title.setText(bluetoothDevice.getName());
        subtitle.setText(bluetoothDevice.getAddress());
        updateConnectionState(false);
    }

    private void updateConnectionState(final boolean connected) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DeviceFragment.this.connected = connected;
                getActivity().invalidateOptionsMenu();
                if (connected) {
                    connectionState.setText(getString(R.string.device_state_connected));
                    connectionState.setTextColor(ContextCompat.getColor(getContext(), R.color.device_state_connected));
                } else {
                    connectionState.setText(getString(R.string.device_state_disconnected));
                    connectionState.setTextColor(ContextCompat.getColor(getContext(), R.color.device_state_disconnected));
                }
            }
        });
    }

    public boolean isConnected() {
        return connected;
    }

    @Override
    public void onResume() {
        super.onStart();
        getActivity().registerReceiver(mPairReceiver, createIntentFilter());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mPairReceiver);
    }


    private static IntentFilter createIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        return intentFilter;
    }

    /**
     * Verify if the bluetooth is enabled
     *
     * @return True if the bluetooth is enabled.
     */
    private boolean isBluetoothEnabled() {
        if (getActivity() == null) {
            Logger.d("getActivity() == null");
            return false;
        }

        // get bluetooth adapter
        BluetoothManager bluetoothManager = (BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();

        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            if (GlobalData.isOnDebugMode()) {
                Logger.d("Bluetooth not enabled");
            }
            return false;
        }

        return true;
    }

}
