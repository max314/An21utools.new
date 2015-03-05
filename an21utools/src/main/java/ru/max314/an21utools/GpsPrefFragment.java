package ru.max314.an21utools;


import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;

import ru.max314.an21utools.util.PreferenceUtils;


/**
 * A simple {@link android.app.Fragment} subclass.
 */
public class GpsPrefFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_gps);
        EditTextPreference preference;
        preference = (EditTextPreference) findPreference(getResources().getString(R.string.pk_gpsActivityShowTime));
        PreferenceUtils.setNumberRange(preference, 0, 60*10);
    }
}
