package com.nicosb.apps.ehcofan.activities;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.nicosb.apps.ehcofan.R;
import com.nicosb.apps.ehcofan.fragments.PlayerInfoFragment;
import com.nicosb.apps.ehcofan.models.Player;
import com.nicosb.apps.ehcofan.models.PlayerWrapper;
import com.nicosb.apps.ehcofan.tasks.FetchPlayersTask;

import java.util.ArrayList;

/**
 * Created by Nico on 22.07.2016.
 */
public class RosterActivity extends AppCompatActivity
                            implements FetchPlayersTask.OnPlayersFetchedListener{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roster);
        FetchPlayersTask fetchPlayersTask = new FetchPlayersTask(this);
        fetchPlayersTask.setOnPlayersFetchedListener(this);
        fetchPlayersTask.execute("");

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
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
    }
}
