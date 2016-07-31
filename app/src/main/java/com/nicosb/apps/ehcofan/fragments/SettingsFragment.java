package com.nicosb.apps.ehcofan.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.nicosb.apps.ehcofan.FirebaseHandler;
import com.nicosb.apps.ehcofan.R;

/**
 * Created by Nico on 28.07.2016.
 */
public class SettingsFragment extends PreferenceFragment
                                implements SharedPreferences.OnSharedPreferenceChangeListener{

    public static final String KEY_PREF_NOTIFICATIONS = "pref_notifications";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preference_screen);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(KEY_PREF_NOTIFICATIONS)){
            if(sharedPreferences.getBoolean(key, false)) {
                FirebaseHandler.signIn(getActivity());
            }
            else{
                FirebaseHandler.signOut(getActivity());
            }
        }
    }
}
