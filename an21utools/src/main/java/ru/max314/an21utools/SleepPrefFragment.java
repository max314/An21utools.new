package ru.max314.an21utools;


import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;

import ru.max314.an21utools.util.tw.PreferenceUtils;


/**
 * A simple {@link android.app.Fragment} subclass.
 */
public class SleepPrefFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_sleep);
        EditTextPreference preference;
        preference = (EditTextPreference) findPreference(getResources().getString(R.string.pk_powerampResumeDelay));
        PreferenceUtils.setNumberRange(preference, 10, 10000);

    }
}
