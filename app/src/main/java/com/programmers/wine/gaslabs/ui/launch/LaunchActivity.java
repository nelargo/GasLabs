package com.programmers.wine.gaslabs.ui.launch;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.programmers.wine.gaslabs.R;
import com.programmers.wine.gaslabs.ui.home.HomeActivity;
import com.programmers.wine.gaslabs.util.BaseActivity;

public class LaunchActivity extends BaseActivity {
    // private static final String TAG_FRAGMENT_LOG_IN = "TagFragmentLogIn";
    private static final String TAG_FRAGMENT_SPLASH = "TagFragmentLogIn";
    private static final int SPLASH_DISPLAY_LENGTH = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        showSplash();

    }

    protected void showSplash() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.content, SplashFragment.newInstance(), TAG_FRAGMENT_SPLASH);
        fragmentTransaction.commit();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showHome();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    protected void showHome() {
        finish();
        startActivity(new Intent(this, HomeActivity.class));
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }
}
