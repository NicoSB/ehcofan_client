package com.nicosb.apps.ehcofan.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.nicosb.apps.ehcofan.R;
import com.nicosb.apps.ehcofan.fragments.ArticlesFragment;
import com.nicosb.apps.ehcofan.fragments.ScheduleFragment;

import java.util.List;
import java.util.logging.Logger;

public class NewsActivity extends AppCompatActivity
                implements NavigationView.OnNavigationItemSelectedListener{
    private String TAG = "NewsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        ArticlesFragment af = new ArticlesFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, af).commit();

        // Initializing Toolbar and setting it as the actionbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = (NavigationView)findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.news);

        DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);

        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.news:
                return true;
            case R.id.schedule:
                Intent scheduleActivity = new Intent(this, ScheduleActivity.class);
                startActivity(scheduleActivity);
                return true;
            case R.id.roster:
                Intent rosterActivity = new Intent(this, RosterActivity.class);
                startActivity(rosterActivity);
                return true;
            case R.id.standings:
                Intent standingsActivity = new Intent(this, StandingsActivity.class);
                startActivity(standingsActivity);
                return true;
            case R.id.settings:
                Intent settingsActivity = new Intent(this, SettingsActivity.class);
                startActivity(settingsActivity);
                return true;
            default:
                return true;
        }
    }
}
