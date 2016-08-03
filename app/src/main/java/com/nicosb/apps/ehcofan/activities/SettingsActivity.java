package com.nicosb.apps.ehcofan.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.nicosb.apps.ehcofan.R;
import com.nicosb.apps.ehcofan.fragments.SettingsFragment;

/**
 * Created by Nico on 28.07.2016.
 */
public class SettingsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getFragmentManager().beginTransaction().replace(R.id.fragment_container_settings, new SettingsFragment()).commit();

        // Initializing Toolbar and setting it as the actionbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = (NavigationView)findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.settings);

        DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);

        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.news:
                Intent newsActivity = new Intent(this, NewsActivity.class);
                startActivity(newsActivity);
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
                return true;
            default:
                return true;
        }
    }
}
