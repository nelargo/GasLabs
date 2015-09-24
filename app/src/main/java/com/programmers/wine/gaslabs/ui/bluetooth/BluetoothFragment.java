package com.programmers.wine.gaslabs.ui.bluetooth;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.programmers.wine.gaslabs.R;
import com.programmers.wine.gaslabs.ui.home.HomeActivity;
import com.programmers.wine.gaslabs.util.BaseFragment;
import com.programmers.wine.gaslabs.util.GlobalData;

public class BluetoothFragment extends BaseFragment {
    protected static final int RES_TITLE = R.string.drawer_item_bluetooth;
    private static final int REQUEST_ENABLE_BLUETOOTH = 1001;
    private Button btnBluetoothControl;

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
        initToolbar(view);
        btnBluetoothControl = (Button) view.findViewById(R.id.btn_bluetooth_control);
        btnBluetoothControl.setVisibility(View.GONE);

        if (isBluetoothEnabled()) {
            searchDevices();
        } else {
            enableBluetooth();
        }

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
                // Bluetooth enabled, search devices.
                searchDevices();
            } else {
                // Bluetooth not enabled, try again button.
                setButtonEnabledBluetooth();
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

    /**
     * Search bluetoothLE devices.
     */
    private void searchDevices() {
        // Search devices.
        setButtonScanDevices();
    }

    private void enableBluetooth() {
        if (!isBluetoothEnabled()) {
            // Enable bluetooth
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BLUETOOTH);
        } else {
            // Bluetooth already enabled
            if (getView() != null) {
                Snackbar.make(getView(), R.string.snack_bar_bluetooth_already_enabled, Snackbar.LENGTH_SHORT).show();
            }
            searchDevices();
        }
    }

    private void setButtonScanDevices() {
        btnBluetoothControl.setVisibility(View.VISIBLE);
        btnBluetoothControl.setText(R.string.action_bluetooth_scan);
        btnBluetoothControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isBluetoothEnabled()) {
                    Toast.makeText(getContext(), R.string.action_bluetooth_scan, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Bluetooth not enabled", Toast.LENGTH_SHORT).show();
                    setButtonEnabledBluetooth();
                }
            }
        });
    }

    private void setButtonEnabledBluetooth() {
        btnBluetoothControl.setVisibility(View.VISIBLE);
        btnBluetoothControl.setText(R.string.action_bluetooth_enable);
        btnBluetoothControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableBluetooth();
            }
        });
    }

    private boolean isBluetoothEnabled() {
        BluetoothManager bluetoothManager = (BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
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
}
