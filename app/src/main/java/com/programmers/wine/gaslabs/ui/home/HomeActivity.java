package com.programmers.wine.gaslabs.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.programmers.wine.gaslabs.R;
import com.programmers.wine.gaslabs.ui.about.AboutActivity;
import com.programmers.wine.gaslabs.ui.bluetooth.BluetoothFragment;
import com.programmers.wine.gaslabs.ui.panel.PanelFragment;
import com.programmers.wine.gaslabs.ui.service.ServiceFragment;
import com.programmers.wine.gaslabs.util.BaseActivity;
import com.programmers.wine.gaslabs.util.GlobalData;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    /**
     * Key to save the id of selected navigation drawer.
     */
    protected static final String SAVED_NAV_ITEM_ID = "SavedNavigationItemId";
    protected static final String TAG_FRAGMENT_PANEL = "TagFragmentPanel";
    protected static final String TAG_FRAGMENT_SERVICE = "TagFragmentService";
    protected static final String TAG_FRAGMENT_BLUETOOTH = "TagFragmentBluetooth";
    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;
    /**
     * Id of selected navigation drawer item or id of default navigation drawer item (Panel).
     */
    protected int navItemId = R.id.drawer_item_panel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        CircleImageView circleImageView = (CircleImageView) navigationView.findViewById(R.id.avatar);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getBaseContext(), "Potato", Toast.LENGTH_SHORT).show();
            }
        });

        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState != null && savedInstanceState.containsKey(SAVED_NAV_ITEM_ID)) {
            if (GlobalData.isOnDebugMode()) {
                Logger.d("Nav item id was recovered");
            }
            navItemId = savedInstanceState.getInt(SAVED_NAV_ITEM_ID);
        }

        setContent(navItemId);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(navigationView)) {
            drawerLayout.closeDrawer(navigationView);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.drawer_sub_item_about:
                // show about activity
                startActivity(new Intent(this, AboutActivity.class));
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                return true;
            case R.id.drawer_sub_item_help:
                // only show feedback to the user
                Snackbar.make(drawerLayout, R.string.drawer_sub_item_help, Snackbar.LENGTH_SHORT).show();
                return true;
            default:
                // load content in the fragment container of home activity
                if (navItemId == menuItem.getItemId()) {
                    Snackbar.make(drawerLayout, R.string.snack_bar_option_already_selected, Snackbar.LENGTH_SHORT).show();
                    return false;
                } else {
                    navItemId = menuItem.getItemId();
                    Snackbar.make(drawerLayout, menuItem.getTitle(), Snackbar.LENGTH_LONG).show();
                    menuItem.setChecked(true);
                    drawerLayout.closeDrawers();
                    setContent(navItemId);
                    return true;
                }
        }
    }

    public DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }

    protected void setContent(int navItemId) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        switch (navItemId) {
            case R.id.drawer_item_panel:
                fragmentTransaction.replace(R.id.content, PanelFragment.newInstance(), TAG_FRAGMENT_PANEL);
                break;
            case R.id.drawer_item_service:
                fragmentTransaction.replace(R.id.content, ServiceFragment.newInstance(), TAG_FRAGMENT_SERVICE);
                break;
            case R.id.drawer_item_bluethooth:
                fragmentTransaction.replace(R.id.content, BluetoothFragment.newInstance(), TAG_FRAGMENT_BLUETOOTH);
                break;
        }
        fragmentTransaction.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (GlobalData.isOnDebugMode()) {
            Logger.d("Save id of navigation item selected");
        }
        outState.putInt(SAVED_NAV_ITEM_ID, navItemId);
        super.onSaveInstanceState(outState);
    }
}
