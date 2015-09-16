package com.programmers.wine.gaslabs.ui.bluetooth;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.programmers.wine.gaslabs.R;
import com.programmers.wine.gaslabs.util.BaseFragment;

public class BluetoothFragment extends BaseFragment {

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_service, container, false);
    }


}
