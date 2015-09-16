package com.programmers.wine.gaslabs.ui.panel;


import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.programmers.wine.gaslabs.R;
import com.programmers.wine.gaslabs.ui.home.HomeActivity;
import com.programmers.wine.gaslabs.util.BaseFragment;

public class PanelFragment extends BaseFragment {

    public static PanelFragment newInstance() {
        PanelFragment fragment = new PanelFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public PanelFragment() {
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
        return inflater.inflate(R.layout.fragment_panel, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolbar(view);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_panel, menu);
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
            case R.id.action_panel:
                Toast.makeText(getActivity(), "Panel RLZ", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initToolbar(View root) {
        Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar);
        if (getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(R.string.app_name);
                actionBar.setIcon(R.mipmap.ic_launcher);
                actionBar.setDisplayHomeAsUpEnabled(true);
            }

        }
    }
}
