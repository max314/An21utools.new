package ru.max314.an21utools.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.preference.EditTextPreference;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;

/**
 * Created by max on 26.02.2015.
 */
public class PreferenceUtils {

    public static void setNumberRange(final EditTextPreference preference,
                                      final Number min, final Number max) {
        setNumberRange(preference, min, max, false);
    }

    public static void setNumberRange(final EditTextPreference preference,
                                      final Number min, final Number max,
                                      final boolean allowEmpty) {

        preference.getEditText().addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(final Editable s) {

                Dialog dialog = preference.getDialog();
                if (dialog instanceof AlertDialog) {
                    Button button = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                    if (allowEmpty && s.length() == 0) {
                        button.setEnabled(true);
                        return;
                    }
                    try {
                        if (min instanceof Integer || max instanceof Integer) {
                            int input = Integer.parseInt(s.toString());
                            button.setEnabled((min == null || input >= min.intValue()) && (max == null || input <= max.intValue()));
                        } else if (min instanceof Float || max instanceof Float) {
                            float input = Float.parseFloat(s.toString());
                            button.setEnabled((min == null || input >= min.floatValue()) && (max == null || input <= max.floatValue()));
                        } else if (min instanceof Double || max instanceof Double) {
                            double input = Double.parseDouble(s.toString());
                            button.setEnabled((min == null || input >= min.doubleValue()) && (max == null || input <= max.doubleValue()));
                        }
                    } catch (Exception e) {
                        button.setEnabled(false);
                    }
                }
            }
        });
    }
}
