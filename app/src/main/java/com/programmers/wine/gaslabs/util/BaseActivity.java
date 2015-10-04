package com.programmers.wine.gaslabs.util;

import android.app.ActivityManager;
import android.content.Context;
import android.support.v4.app.ActivityManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.programmers.wine.gaslabs.R;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class BaseActivity extends AppCompatActivity {
    protected MaterialDialog dialog;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    /*
    * Feedback methods.
    */
    protected MaterialDialog createDialog() {
        return new MaterialDialog.Builder(getBaseContext())
                .content(R.string.loading)
                .contentColor(ContextCompat.getColor(getBaseContext(), android.R.color.black))
                .progress(true, 0)
                .backgroundColor(ContextCompat.getColor(getBaseContext(), android.R.color.white))
                .widgetColor(ContextCompat.getColor(getBaseContext(), R.color.feedback_loading_progress))
                .build();
    }

    protected void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    protected void showDialog() {
        if (dialog != null) {
            dialog.show();
        } else {
            dialog = createDialog();
            dialog.show();
        }
    }

    protected boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
