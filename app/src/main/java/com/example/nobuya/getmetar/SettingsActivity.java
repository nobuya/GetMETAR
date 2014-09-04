package com.example.nobuya.getmetar;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

/**
 * Created by nobuya on 2014/09/04.
 */
//public class SettingsActivity extends PreferenceActivity {
public class SettingsActivity extends Activity {
    private PrefFragment fragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //addPreferencesFromResource(R.xml.preference);
        fragment = new PrefFragment();
        getFragmentManager().beginTransaction().replace(android.R.id
                .content, fragment).commit();
    }

    public void setChanged(boolean v) {
        Intent intent = new Intent();
        setResult(v ? RESULT_OK : RESULT_CANCELED, intent);
    }

    public static class PrefFragment extends PreferenceFragment implements
            SharedPreferences.OnSharedPreferenceChangeListener {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preference);
        }

        @Override
        public void onResume() {
            super.onResume();
            resetSummary();
            getPreferenceScreen().getSharedPreferences()
                    .registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceScreen().getSharedPreferences()
                    .unregisterOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences
                                                             paramSharePreferences, String paramString) {
            resetSummary();
            ((SettingsActivity)getActivity()).setChanged(true);
        }

        public void resetSummary() {
            SharedPreferences sharedPrefs = getPreferenceManager()
                    .getSharedPreferences();
            PreferenceScreen screen = this.getPreferenceScreen();
            for (int i = 0; i < screen.getPreferenceCount(); i++) {
                Preference pref = screen.getPreference(i);
                if (pref instanceof CheckBoxPreference) continue;
                String key = pref.getKey();
                String val = sharedPrefs.getString(key, "");
                pref.setSummary(val);
            }
        }
    }

    @Override
    protected void onDestroy() {
//        if (popupWindow != null && popupWindow.isShowing()) {
//            popupWindow.dismiss();
//        }
        super.onDestroy();
    }
}
