package com.programmers.wine.gaslabs.ui.bluetooth;


import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.programmers.wine.gaslabs.R;
import com.programmers.wine.gaslabs.ui.home.HomeActivity;
import com.programmers.wine.gaslabs.util.BaseFragment;
import com.programmers.wine.gaslabs.util.Utils;


public class DeviceFragment extends BaseFragment implements View.OnClickListener {
    protected static final int RES_TITLE = R.string.title_fragment_device;
    private BluetoothDevice bluetoothDevice;
    private boolean showToolbar;
    private Button btnDeviceControl;
    // private ImageView icon;
    private TextView title;
    private TextView subtitle;

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
            Toast.makeText(getContext(), btnDeviceControl.getText(), Toast.LENGTH_SHORT).show();
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
        // icon = (ImageView) root.findViewById(R.id.device_icon);
        title = (TextView) root.findViewById(R.id.device_title);
        subtitle = (TextView) root.findViewById(R.id.device_subtitle);
        btnDeviceControl = (Button) root.findViewById(R.id.btn_device_control);

        btnDeviceControl.setOnClickListener(this);
    }

    private void populateView(BluetoothDevice bluetoothDevice) {
        if (bluetoothDevice != null) {
            title.setText(bluetoothDevice.getName());
            subtitle.setText(bluetoothDevice.getAddress());
            if (!isDeviceConnected()) {
                btnDeviceControl.setText(R.string.action_device_connect);
                Utils.setColorButton(getContext(), btnDeviceControl, R.color.colorButtonPrimary, R.drawable.btn_primary);
            } else {
                btnDeviceControl.setText(R.string.action_device_connect);
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
}
