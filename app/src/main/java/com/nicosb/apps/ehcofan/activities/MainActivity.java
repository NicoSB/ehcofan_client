package com.nicosb.apps.ehcofan.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.nicosb.apps.ehcofan.FirebaseHandler;
import com.nicosb.apps.ehcofan.R;

public class MainActivity extends AppCompatActivity {
    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        PreferenceManager.setDefaultValues(this, R.xml.preference_screen, false);
        FirebaseHandler.signIn(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseHandler.mAuth != null){
            FirebaseHandler.mAuth.addAuthStateListener(FirebaseHandler.mAuthListener);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(FirebaseHandler.mAuthListener != null){
            FirebaseHandler.mAuth.removeAuthStateListener(FirebaseHandler.mAuthListener);
        }
    }
    public void openNewsActivity(View view) {
        Intent newsActivity = new Intent(this, NewsActivity.class);
        startActivity(newsActivity);
    }

    public void openScheduleActivity(View view) {
        Intent scheduleActivity = new Intent(this, ScheduleActivity.class);
        startActivity(scheduleActivity);
    }

    public void openRosterActivity(View view) {
        Intent scheduleActivity = new Intent(this, RosterActivity.class);
        startActivity(scheduleActivity);
    }

    public void openStandingsActivity(View view) {
        Intent standingsActivity = new Intent(this, StandingsActivity.class);
        startActivity(standingsActivity);
    }

    public void openSettingsActivity(View view) {
        Intent settingsActivity = new Intent(this, SettingsActivity.class);
        startActivity(settingsActivity);
    }
}
