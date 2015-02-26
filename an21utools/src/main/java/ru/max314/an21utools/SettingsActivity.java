package ru.max314.an21utools;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import java.util.List;

public class SettingsActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Populate the activity with the top-level headers.
     */
    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_head, target);

    }

    @Override
    public boolean onIsMultiPane() {
        return true;
    }
}
