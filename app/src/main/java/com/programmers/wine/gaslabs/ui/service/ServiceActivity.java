package com.programmers.wine.gaslabs.ui.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.programmers.wine.gaslabs.R;
import com.programmers.wine.gaslabs.util.BaseActivity;
import com.programmers.wine.gaslabs.util.Utils;

public class ServiceActivity extends BaseActivity implements View.OnClickListener {
    private static final int RESOURCE_TITLE = R.string.title_activity_service;
    private Button btnStart;
    private Button btnStop;

    private LocalService localService;
    private boolean boundState = false;

    /**
     * Define callbacks for service binding.
     */
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            if (iBinder instanceof LocalService.LocalBinder) {
                LocalService.LocalBinder localBinder = (LocalService.LocalBinder) iBinder;
                localService = localBinder.getService();
                boundState = true;
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            boundState = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        initToolbar();

        btnStart = (Button) findViewById(R.id.btn_start_service);
        btnStop = (Button) findViewById(R.id.btn_stop_service);
        Utils.setColorButton(getBaseContext(), btnStart, R.color.colorButtonPrimary, R.drawable.btn_primary);
        Utils.setColorButton(getBaseContext(), btnStop, R.color.colorButtonSecondary, R.drawable.btn_primary);
        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);

        if (isMyServiceRunning(GasLabsService.class)) {
            Toast.makeText(getBaseContext(), "The GasLabsService is already running", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_service, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_start_service:
                Toast.makeText(this, R.string.action_start_service, Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_get_from_bound_service:
                Toast.makeText(this, "Random number = " + localService.getRandomNumber(), Toast.LENGTH_SHORT).show();
                return true;
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    @Override
    public void onClick(View view) {
        if (view == btnStart) {
            startStatedService();
        }

        if (view == btnStop) {
            stopStartedService();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        bind();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbind();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(RESOURCE_TITLE);
            actionBar.setIcon(R.mipmap.ic_launcher);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    private void startStatedService() {
        Intent intentForService = new Intent(getBaseContext(), GasLabsService.class);
        startService(intentForService);
    }

    private void stopStartedService() {
        stopService(new Intent(getBaseContext(), GasLabsService.class));
    }

    /**
     * Bind to local service.
     */
    private void bind() {
        Intent intent = new Intent(this, LocalService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * Unbind service.
     */
    private void unbind() {
        if (boundState) {
            unbindService(serviceConnection);
            boundState = false;
        }
    }

}
