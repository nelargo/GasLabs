package com.programmers.wine.gaslabs.ui.bluetooth;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import com.orhanobut.logger.Logger;
import com.programmers.wine.gaslabs.R;
import com.programmers.wine.gaslabs.ui.bluetooth.service.XiaomiService;
import com.programmers.wine.gaslabs.util.BaseSwipeBackActivity;
import com.programmers.wine.gaslabs.util.GlobalData;

public class DeviceActivity extends BaseSwipeBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);

        if (isMyServiceRunning(XiaomiService.class)) {
            Logger.d("Xiaomi service already running");
        } else {
            Logger.d("Xiaomi service not running");
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.content, DeviceFragment.newInstance(GlobalData.getInstance().getSelectedBluetoothDevice(), true));
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_device, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }
}
