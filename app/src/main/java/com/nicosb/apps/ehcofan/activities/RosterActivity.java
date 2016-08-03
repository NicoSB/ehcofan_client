package com.nicosb.apps.ehcofan.activities;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.nicosb.apps.ehcofan.R;
import com.nicosb.apps.ehcofan.fragments.PlayerInfoFragment;
import com.nicosb.apps.ehcofan.fragments.ScheduleFragment;
import com.nicosb.apps.ehcofan.models.Player;
import com.nicosb.apps.ehcofan.tasks.FetchPlayersTask;
import com.nicosb.apps.ehcofan.views.PlayerView;

import java.util.ArrayList;

/**
 * Created by Nico on 22.07.2016.
 */
public class RosterActivity extends AppCompatActivity
                            implements FetchPlayersTask.OnPlayersFetchedListener,
                            NavigationView.OnNavigationItemSelectedListener{
    private ProgressBar progressBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roster);
        FetchPlayersTask fetchPlayersTask = new FetchPlayersTask(this);
        fetchPlayersTask.setOnPlayersFetchedListener(this);
        fetchPlayersTask.execute("");

        progressBar = new ProgressBar(this);
        LinearLayout container = (LinearLayout)findViewById(R.id.container_roster);
        container.addView(progressBar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = (NavigationView)findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.roster);

        DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);

        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onPlayersFetched(ArrayList<Player> players) {
        LinearLayout container = (LinearLayout)findViewById(R.id.container_roster);

        for(final Player p: players){
            PlayerView playerView = new PlayerView(this, p);
            playerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogFragment playerInfo = new PlayerInfoFragment();
                    Bundle args = new Bundle();
                    args.putParcelable("player", p);
                    playerInfo.setArguments(args);
                    playerInfo.show(getFragmentManager(), "player");
                }
            });
            container.addView(playerView);
        }
        container.removeView(progressBar);
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
