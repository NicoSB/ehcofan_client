package com.nicosb.apps.ehcofan.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.nicosb.apps.ehcofan.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void openNewsActivity(View view) {
        Intent newsActivity = new Intent(this, NewsActivity.class);
        startActivity(newsActivity);
    }

    public void openScheduleActivity(View view) {
        Intent scheduleActivity = new Intent(this, ScheduleActivity.class);
        startActivity(scheduleActivity);
    }
}
