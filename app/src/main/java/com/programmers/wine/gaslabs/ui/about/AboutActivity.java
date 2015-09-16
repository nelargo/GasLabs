package com.programmers.wine.gaslabs.ui.about;

import android.os.Bundle;

import com.programmers.wine.gaslabs.R;
import com.programmers.wine.gaslabs.util.BaseActivity;

import de.psdev.licensesdialog.LicensesDialog;

public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        showLicences();
    }

    private void showLicences() {
        LicensesDialog.Builder builder = new LicensesDialog.Builder(this);
        builder.setTitle(R.string.dialog_licenses_title);
        builder.setNotices(R.raw.noticies);
        builder.build().show();
    }
}
