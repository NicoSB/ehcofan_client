package com.nicosb.apps.ehcofan;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.StringRes;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.nicosb.apps.ehcofan.activities.HomeActivity;
import com.nicosb.apps.ehcofan.activities.NewsActivity;
import com.nicosb.apps.ehcofan.activities.RosterActivity;
import com.nicosb.apps.ehcofan.activities.ScheduleActivity;
import com.nicosb.apps.ehcofan.activities.SettingsActivity;
import com.nicosb.apps.ehcofan.activities.StandingsActivity;

public class ToolbarHelper {

    public static DrawerLayout loadToolbar(AppCompatActivity activity) {
        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);

        DrawerLayout drawerLayout = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
        DrawerToggle drawerToggle = new DrawerToggle(activity, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        NavigationView navigationView = (NavigationView) activity.findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(drawerToggle);
        // disabled due to not working correctly
        // navigationView.setCheckedItem(getItemId(activity));

        return drawerLayout;
    }

    private static int getItemId(AppCompatActivity activity) {
        int id = -1;
        if (activity instanceof RosterActivity) {
            id =  R.id.roster;
        }
        if (activity instanceof NewsActivity) {
            id =  R.id.news;
        }
        if (activity instanceof ScheduleActivity) {
            id =  R.id.schedule;
        }
        if (activity instanceof StandingsActivity) {
            id =  R.id.standings;
        }
        if (activity instanceof HomeActivity) {
            id = R.id.home;
        }
        return id;
    }

    private static class DrawerToggle extends ActionBarDrawerToggle
            implements NavigationView.OnNavigationItemSelectedListener {
        private AppCompatActivity activity;

        public DrawerToggle(Activity activity, DrawerLayout drawerLayout, @StringRes int openDrawerContentDescRes, @StringRes int closeDrawerContentDescRes) {
            super(activity, drawerLayout, openDrawerContentDescRes, closeDrawerContentDescRes);
            this.activity = (AppCompatActivity)activity;
        }

        DrawerToggle(Activity activity, DrawerLayout drawerLayout, Toolbar toolbar, @StringRes int openDrawerContentDescRes, @StringRes int closeDrawerContentDescRes) {
            super(activity, drawerLayout, toolbar, openDrawerContentDescRes, closeDrawerContentDescRes);
            this.activity = (AppCompatActivity)activity;
        }

        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            Intent newActivity = null;
            switch (item.getItemId()) {
                case R.id.home:
                    newActivity = new Intent(activity, HomeActivity.class);
                    break;
                case R.id.news:
                    newActivity = new Intent(activity, NewsActivity.class);
                    break;
                case R.id.schedule:
                    newActivity = new Intent(activity, ScheduleActivity.class);
                    break;
                case R.id.roster:
                    newActivity = new Intent(activity, RosterActivity.class);
                    break;
                case R.id.standings:
                    newActivity = new Intent(activity, StandingsActivity.class);
                    break;
                case R.id.settings:
                    newActivity = new Intent(activity, SettingsActivity.class);
                    break;
            }
            if (newActivity == null || activity.getClass().getName().equals(newActivity.getComponent().getClassName())) {
                return false;
            } else {
                activity.startActivity(newActivity);
                return true;
            }
        }
    }
}