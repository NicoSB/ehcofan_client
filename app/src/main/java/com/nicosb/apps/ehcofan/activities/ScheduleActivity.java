package com.nicosb.apps.ehcofan.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.nicosb.apps.ehcofan.R;
import com.nicosb.apps.ehcofan.fragments.ScheduleFragment;

public class ScheduleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        ScheduleFragment scheduleFragment = new ScheduleFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.schedule_fragment_container, scheduleFragment).commit();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
    }
}
