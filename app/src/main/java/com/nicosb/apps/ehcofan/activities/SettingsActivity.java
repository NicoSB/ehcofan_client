package com.nicosb.apps.ehcofan.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.nicosb.apps.ehcofan.R;
import com.nicosb.apps.ehcofan.fragments.SettingsFragment;

/**
 * Created by Nico on 28.07.2016.
 */
public class SettingsActivity extends AppCompatActivity{

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getFragmentManager().beginTransaction().replace(R.id.fragment_container_settings, new SettingsFragment()).commit();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
    }
}
