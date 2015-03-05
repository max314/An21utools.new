package ru.max314.an21utools;


import android.os.Bundle;
import android.app.Fragment;
import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;

import ru.max314.an21utools.util.PreferenceUtils;


/**
 * A simple {@link Fragment} subclass.
 */
public class AutorunPrefFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_autorun);
        EditTextPreference preference;
        preference = (EditTextPreference) findPreference(getResources().getString(R.string.pk_startDelay));
        PreferenceUtils.setNumberRange(preference, 100, 10000);
        preference = (EditTextPreference) findPreference(getResources().getString(R.string.pk_applicationDelay));
        PreferenceUtils.setNumberRange(preference,100,10000);
    }
}
