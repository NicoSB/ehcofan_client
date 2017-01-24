package com.nicosb.apps.ehcofan.activities;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;

import com.nicosb.apps.ehcofan.FirebaseHandler;
import com.nicosb.apps.ehcofan.R;
import com.nicosb.apps.ehcofan.ToolbarHelper;

/**
 * Created by Nico on 23.01.2017.
 */

public class SettingsActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ToolbarHelper.loadToolbar(this);
        getFragmentManager().beginTransaction().replace(R.id.container_settings, new SettingsFragment()).commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseHandler.signIn(this);
    }

    public static class SettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preference_screen);
        }
    }
}
