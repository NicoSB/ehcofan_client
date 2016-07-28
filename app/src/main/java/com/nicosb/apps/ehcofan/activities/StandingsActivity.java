package com.nicosb.apps.ehcofan.activities;

import android.app.ActionBar;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.nicosb.apps.ehcofan.R;
import com.nicosb.apps.ehcofan.models.Team;
import com.nicosb.apps.ehcofan.models.TeamAdapter;
import com.nicosb.apps.ehcofan.tasks.FetchStandingsTask;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Nico on 22.07.2016.
 */
public class StandingsActivity extends AppCompatActivity
                                implements FetchStandingsTask.OnTeamsFetchedListener{
    private ProgressBar progressBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standings);
        FetchStandingsTask fetchStandingsTask = new FetchStandingsTask(this);
        fetchStandingsTask.setOnTeamsFetchedListener(this);
        fetchStandingsTask.execute("");

        progressBar = new ProgressBar(this);
        LinearLayout container = (LinearLayout)findViewById(R.id.ll_standings);
        container.addView(progressBar);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
    }

    @Override
    public void onTeamsFetched(ArrayList<Team> teams) {
        TableLayout container = (TableLayout)findViewById(R.id.container_standings);
        int position = 1;

        for(Team t: teams){
            View tableRow = getLayoutInflater().inflate(R.layout.listitem_team, null, false);

            TextView txt_rank = (TextView)tableRow.findViewById(R.id.txt_rank);
            TextView txt_name =  (TextView)tableRow.findViewById(R.id.txt_team_name);
            TextView txt_games = (TextView)tableRow.findViewById(R.id.txt_games);
            TextView txt_wins = (TextView)tableRow.findViewById(R.id.txt_wins);
            TextView txt_ot_wins = (TextView)tableRow.findViewById(R.id.txt_ot_wins);
            TextView txt_ot_losses = (TextView)tableRow.findViewById(R.id.txt_ot_losses);
            TextView txt_losses = (TextView)tableRow.findViewById(R.id.txt_losses);
            TextView txt_goals = (TextView)tableRow.findViewById(R.id.txt_goals);
            TextView txt_points =  (TextView)tableRow.findViewById(R.id.txt_points);

            txt_rank.setText(String.format(Locale.GERMANY, "%d.", position));
            txt_name.setText(t.getName());
            txt_games.setText(String.valueOf(t.getGames()));
            txt_goals.setText(String.format(Locale.GERMANY, "%d:%d", t.getGoals_for(), t.getGoals_against()));
            txt_points.setText(String.valueOf(t.getPoints()));

            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                txt_wins.setText(String.valueOf(t.getWins()));
                txt_ot_wins.setText(String.valueOf(t.getOt_wins()));
                txt_ot_losses.setText(String.valueOf(t.getOt_losses()));
                txt_losses.setText(String.valueOf(t.getLosses()));
            }

            if(t.getName().equals("EHC Olten")){
                txt_name.setTextColor(getResources().getColor(R.color.mainGreen));
                txt_name.setTypeface(Typeface.DEFAULT_BOLD);
                txt_rank.setTextColor(getResources().getColor(R.color.mainGreen));
                txt_rank.setTypeface(Typeface.DEFAULT_BOLD);
                txt_games.setTextColor(getResources().getColor(R.color.mainGreen));
                txt_games.setTypeface(Typeface.DEFAULT_BOLD);
                txt_goals.setTextColor(getResources().getColor(R.color.mainGreen));
                txt_goals.setTypeface(Typeface.DEFAULT_BOLD);
                txt_points.setTextColor(getResources().getColor(R.color.mainGreen));
                txt_points.setTypeface(Typeface.DEFAULT_BOLD);

                if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    txt_wins.setTextColor(getResources().getColor(R.color.mainGreen));
                    txt_wins.setTypeface(Typeface.DEFAULT_BOLD);
                    txt_ot_wins.setTextColor(getResources().getColor(R.color.mainGreen));
                    txt_ot_wins.setTypeface(Typeface.DEFAULT_BOLD);
                    txt_losses.setTextColor(getResources().getColor(R.color.mainGreen));
                    txt_losses.setTypeface(Typeface.DEFAULT_BOLD);
                    txt_ot_losses.setTextColor(getResources().getColor(R.color.mainGreen));
                    txt_ot_losses.setTypeface(Typeface.DEFAULT_BOLD);
                }
            }

            container.addView(tableRow);
            position++;
        }

        LinearLayout ll_standings = (LinearLayout)findViewById(R.id.ll_standings);
        ll_standings.removeView(progressBar);
    }
}
