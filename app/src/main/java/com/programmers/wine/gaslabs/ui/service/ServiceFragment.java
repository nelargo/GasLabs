package com.programmers.wine.gaslabs.ui.service;


import android.content.Intent;
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

import com.programmers.wine.gaslabs.R;
import com.programmers.wine.gaslabs.ui.home.HomeActivity;
import com.programmers.wine.gaslabs.util.BaseFragment;

public class ServiceFragment extends BaseFragment {
    protected static final int RES_TITLE = R.string.drawer_item_service;
    private Button serviceBtn;

    public static ServiceFragment newInstance() {
        ServiceFragment fragment = new ServiceFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public ServiceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_service, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolbar(view);

        serviceBtn = (Button) view.findViewById(R.id.btn_service);
        serviceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showServiceActivity();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_service, menu);
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
            case R.id.action_start_service:
                showServiceActivity();
                return true;
        }
        return super.onOptionsItemSelected(item);
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

    private void showServiceActivity() {
        Intent intentServiceActivity = new Intent(getActivity(), ServiceActivity.class);
        startActivity(intentServiceActivity);
        getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }
}
