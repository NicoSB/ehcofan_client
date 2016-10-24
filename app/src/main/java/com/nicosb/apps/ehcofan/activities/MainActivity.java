package com.nicosb.apps.ehcofan.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nicosb.apps.ehcofan.R;
import com.nicosb.apps.ehcofan.models.Match;
import com.nicosb.apps.ehcofan.models.Player;
import com.nicosb.apps.ehcofan.models.StandingsTeam;
import com.nicosb.apps.ehcofan.tasks.FetchMatchesTask;
import com.nicosb.apps.ehcofan.tasks.FetchPlayersTask;
import com.nicosb.apps.ehcofan.tasks.FetchStandingsTask;

import java.util.ArrayList;

/**
 * Created by Nico on 17.09.2016.
 */
public class MainActivity extends AppCompatActivity
        implements FetchStandingsTask.OnTeamsFetchedListener, FetchMatchesTask.OnScheduleFetchedListener, FetchPlayersTask.OnPlayersFetchedListener {

    private ProgressBar mProgress;
    private int mProgressStatus = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

            mProgress = (ProgressBar) findViewById(R.id.main_progressbar);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            fetchTeams();
            fetchSchedule();
            fetchPlayers();
        } else {
            Toast.makeText(this, "Keine Verbindung zum Internet", Toast.LENGTH_SHORT).show();
            updateProgressStatus(100);
        }
    }

    private void fetchPlayers() {
        FetchPlayersTask fetchPlayersTask = new FetchPlayersTask(this);
        fetchPlayersTask.setOnPlayersFetchedListener(this);
        fetchPlayersTask.execute("" );
    }

    private void fetchSchedule() {
        FetchMatchesTask fetchMatchesTask = new FetchMatchesTask(this);
        fetchMatchesTask.setOnScheduleFetchedListener(this);
        fetchMatchesTask.execute("" );
    }

    private void fetchTeams() {
        FetchStandingsTask fetchStandingsTask = new FetchStandingsTask(this);
        fetchStandingsTask.setOnTeamsFetchedListener(this);
        fetchStandingsTask.execute("NLB%2016/17" );
    }

    @Override
    public void onTeamsFetched(ArrayList<StandingsTeam> standingsTeams) {
        updateProgressStatus(33);
    }

    @Override
    public void onScheduleFetched(ArrayList<Match> matches) {
        updateProgressStatus(34);
    }

    @Override
    public void onPlayersFetched(ArrayList<Player> players) {
        updateProgressStatus(33);
    }

    private void updateProgressStatus(int step) {
        mProgressStatus += step;
        mProgress.setProgress(mProgressStatus);

        if (mProgressStatus >= 99) {
            Intent homeActivity = new Intent(this, HomeActivity.class);
            startActivity(homeActivity);
        }
    }
}
