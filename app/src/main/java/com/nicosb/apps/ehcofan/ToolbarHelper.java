package com.nicosb.apps.ehcofan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.StringRes;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;

import com.nicosb.apps.ehcofan.activities.CupActivity;
import com.nicosb.apps.ehcofan.activities.NewsActivity;
import com.nicosb.apps.ehcofan.activities.RosterActivity;
import com.nicosb.apps.ehcofan.activities.ScheduleActivity;
import com.nicosb.apps.ehcofan.activities.StandingsActivity;
import com.nicosb.apps.ehcofan.tasks.FetchPlayersTask;

public class ToolbarHelper{

    public static DrawerLayout loadToolbar(AppCompatActivity activity){
        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);

        DrawerLayout drawerLayout = (DrawerLayout)activity.findViewById(R.id.drawer_layout);
        DrawerToggle drawerToggle = new DrawerToggle(activity, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        NavigationView navigationView = (NavigationView)activity.findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(drawerToggle);
        navigationView.setCheckedItem(getItemId(activity));

        return drawerLayout;
    }

    private static int getItemId(AppCompatActivity activity) {
        if(activity instanceof RosterActivity){
            return R.id.roster;
        }
        if(activity instanceof NewsActivity){
            return R.id.news;
        }
        if(activity instanceof ScheduleActivity){
            return R.id.schedule;
        }
        if(activity instanceof StandingsActivity){
            return R.id.standings;
        }
        if(activity instanceof CupActivity){
            return R.id.cup;
        }
        return -1;
    }

    public static class DrawerToggle extends ActionBarDrawerToggle
            implements NavigationView.OnNavigationItemSelectedListener{
        private Activity activity;

        public DrawerToggle(Activity activity, DrawerLayout drawerLayout, @StringRes int openDrawerContentDescRes, @StringRes int closeDrawerContentDescRes) {
            super(activity, drawerLayout, openDrawerContentDescRes, closeDrawerContentDescRes);
            this.activity = activity;
        }

        public DrawerToggle(Activity activity, DrawerLayout drawerLayout, Toolbar toolbar, @StringRes int openDrawerContentDescRes, @StringRes int closeDrawerContentDescRes) {
            super(activity, drawerLayout, toolbar, openDrawerContentDescRes, closeDrawerContentDescRes);
            this.activity = activity;
        }


        @Override
        public void onDrawerClosed(View drawerView) {
                Switch notificationsSwitch = (Switch)drawerView.findViewById(R.id.switch_notifications);

            FirebaseHandler.sign(activity, notificationsSwitch.isChecked());

            SharedPreferences sharedPreferences = activity.getSharedPreferences(FetchPlayersTask.CUSTOM_PREFS, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(FirebaseHandler.PREF_NOTIFICATIONS, notificationsSwitch.isChecked()).apply();
            super.onDrawerClosed(drawerView);
        }

        @Override
        public void onDrawerOpened(View drawerView) {
            super.onDrawerOpened(drawerView);

            Switch notificationsSwitch = (Switch)drawerView.findViewById(R.id.switch_notifications);
            SharedPreferences prefs = activity.getSharedPreferences(FetchPlayersTask.CUSTOM_PREFS, Context.MODE_PRIVATE);
            notificationsSwitch.setChecked(prefs.getBoolean(FirebaseHandler.PREF_NOTIFICATIONS, false));
        }

        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            Intent newActivity = null;
            switch (item.getItemId()) {
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
                case R.id.cup:
                    newActivity = new Intent(activity, CupActivity.class);
                    break;
            }
            if(newActivity == null || activity.getClass().getName().equals(newActivity.getComponent().getClassName())){
                return false;
            }
            else{
                activity.startActivity(newActivity);
                return true;
            }
        }
    }
}