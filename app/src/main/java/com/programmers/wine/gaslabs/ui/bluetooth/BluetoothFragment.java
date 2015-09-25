package com.programmers.wine.gaslabs.ui.bluetooth;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.programmers.wine.gaslabs.R;
import com.programmers.wine.gaslabs.ui.home.HomeActivity;
import com.programmers.wine.gaslabs.util.BaseFragment;
import com.programmers.wine.gaslabs.util.DividerItemDecoration;
import com.programmers.wine.gaslabs.util.GlobalData;
import com.programmers.wine.gaslabs.util.ItemClickSupport;

public class BluetoothFragment extends BaseFragment {
    protected static final int RES_TITLE = R.string.drawer_item_bluetooth;
    private static final int REQUEST_ENABLE_BLUETOOTH = 1001;
    /**
     * Scanning bluetooth devices in milliseconds.
     */
    private static final long SCAN_PERIOD = 10000;
    private Button btnBluetoothControl;
    private boolean scanning = false;
    private Handler scanHandler;
    private BluetoothAdapter bluetoothAdapter;
    private ScanDeviceAdapter scanDeviceAdapter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice bluetoothDevice, int rssi, byte[] scanRecord) {
            // add device to the list
            if (scanDeviceAdapter != null) {
                scanDeviceAdapter.addDevice(bluetoothDevice);
                scanDeviceAdapter.notifyDataSetChanged();
            }
        }
    };

    public static BluetoothFragment newInstance() {
        BluetoothFragment fragment = new BluetoothFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public BluetoothFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_bluetooth, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // init views
        initToolbar(view);
        initViews(view);

        // create scan handler instance
        scanHandler = new Handler();

        if (!isBluetoothEnabled()) {
            // Bluetooth not enabled, we must enabled it
            enableBluetooth();
        } else {
            // Bluetooth already enabled, scan
            scanDevices(true);
            // Snackbar.make(getView(), R.string.snack_bar_bluetooth_already_enabled, Snackbar.LENGTH_SHORT).show();
        }

    }

    private void initViews(View root) {
        btnBluetoothControl = (Button) root.findViewById(R.id.btn_bluetooth_control);
        progressBar = (ProgressBar) root.findViewById(R.id.progress);
        recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);

        progressBar.setVisibility(View.GONE);
        btnBluetoothControl.setVisibility(View.GONE);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), null));
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        // Initialize recycler view adapter
        scanDeviceAdapter = new ScanDeviceAdapter();
        recyclerView.setAdapter(scanDeviceAdapter);
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                BluetoothDevice bluetoothDevice = scanDeviceAdapter.getDevice(position);
                String msg = "Error";
                if (bluetoothDevice != null) {
                    msg = bluetoothDevice.getName();
                }
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_bluetooth, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (getActivity() instanceof HomeActivity) {
                    ((HomeActivity) getActivity()).getDrawerLayout().openDrawer(GravityCompat.START);
                }
                return true;
            case R.id.action_enable_bluetooth:
                Toast.makeText(getActivity(), R.string.action_bluetooth_enable, Toast.LENGTH_SHORT).show();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BLUETOOTH) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                // Bluetooth enabled, scan devices.
                scanDevices(true);
            } else {
                // Bluetooth not enabled, try again button.
                showButtonEnabledBluetooth();
            }
        }

    }

    @Override
    protected void onAnimationStarted() {
        super.onAnimationStarted();
        if (getActivity() instanceof HomeActivity) {
            HomeActivity homeActivity = (HomeActivity) getActivity();
            if (homeActivity.getDrawerLayout().isDrawerOpen(GravityCompat.START)) {
                homeActivity.getDrawerLayout().closeDrawers();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (isScanning()) {
            scanDevices(false);
        }
    }

    private void initToolbar(View root) {
        Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar);
        if (getActivity() instanceof AppCompatActivity) {
            HomeActivity homeActivity = ((HomeActivity) getActivity());
            homeActivity.setSupportActionBar(toolbar);
            ActionBar actionBar = homeActivity.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(RES_TITLE);
                actionBar.setIcon(R.mipmap.ic_launcher);
                ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), homeActivity.getDrawerLayout(), toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
                homeActivity.getDrawerLayout().setDrawerListener(actionBarDrawerToggle);
                actionBarDrawerToggle.syncState();
            }

        }
    }

    private void enableBluetooth() {
        // Enable bluetooth
        Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableIntent, REQUEST_ENABLE_BLUETOOTH);
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
        bluetoothAdapter = bluetoothManager.getAdapter();

        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            if (GlobalData.isOnDebugMode()) {
                Logger.d("Bluetooth not enabled");
            }
            return false;
        }
        if (GlobalData.isOnDebugMode()) {
            Logger.d("Bluetooth enabled");
        }
        return true;
    }

    private void showButtonScanDevices() {
        btnBluetoothControl.setVisibility(View.VISIBLE);
        btnBluetoothControl.setText(R.string.action_bluetooth_scan);
        btnBluetoothControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isBluetoothEnabled()) {
                    scanDevices(true);
                } else {
                    showButtonEnabledBluetooth();
                }
            }
        });
    }

    private void showButtonEnabledBluetooth() {
        btnBluetoothControl.setVisibility(View.VISIBLE);
        btnBluetoothControl.setText(R.string.action_bluetooth_enable);
        btnBluetoothControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isBluetoothEnabled()) {
                    enableBluetooth();
                } else {
                    showButtonScanDevices();
                }
            }
        });
    }

    private void scanDevices(boolean enable) {
        if (enable) {
            // what we do when the time is over
            scanHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Stop looking for bluetooth devices
                    scanning = false;
                    bluetoothAdapter.stopLeScan(leScanCallback);

                    if (scanDeviceAdapter == null || scanDeviceAdapter.getItemCount() == 0) {
                        showButtonScanDevices();
                        recyclerView.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                    }

                }
            }, SCAN_PERIOD);

            // what we do when we need scan for bluetooth le devices.
            if (isBluetoothEnabled()) {
                scanning = true;
                btnBluetoothControl.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                bluetoothAdapter.startLeScan(leScanCallback);
            } else {
                showButtonEnabledBluetooth();
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
            }
        }
    }

    public boolean isScanning() {
        return scanning;
    }

}
