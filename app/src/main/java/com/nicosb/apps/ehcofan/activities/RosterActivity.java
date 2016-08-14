package com.nicosb.apps.ehcofan.activities;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.NavigationView;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.nicosb.apps.ehcofan.R;
import com.nicosb.apps.ehcofan.fragments.PlayerInfoFragment;
import com.nicosb.apps.ehcofan.models.Player;
import com.nicosb.apps.ehcofan.tasks.FetchPlayersTask;
import com.nicosb.apps.ehcofan.views.PlayerView;
import com.nicosb.apps.ehcofan.ToolbarHelper;
import com.nicosb.apps.ehcofan.ToolbarHelper.DrawerToggle;

import java.util.ArrayList;

/**
 * Created by Nico on 22.07.2016.
 */
public class RosterActivity extends AppCompatActivity
                            implements FetchPlayersTask.OnPlayersFetchedListener{
    private ProgressBar progressBar;
    DrawerLayout drawerLayout;

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

        drawerLayout = ToolbarHelper.loadToolbar(this);
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
    protected void onStop() {
        super.onStop();
        drawerLayout.closeDrawer(Gravity.LEFT, false);
    }
}
