package com.nicosb.apps.ehcofan.tasks;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.nicosb.apps.ehcofan.CacheDBHelper;
import com.nicosb.apps.ehcofan.Cacher;
import com.nicosb.apps.ehcofan.R;
import com.nicosb.apps.ehcofan.models.StandingsTeam;
import com.nicosb.apps.ehcofan.models.TeamWrapper;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Nico on 27.07.2016.
 */
public class FetchStandingsTask extends AsyncTask<String, Void, ArrayList<StandingsTeam>> {
    Context context;
    OnTeamsFetchedListener onTeamsFetchedListener;
    private boolean onlyOffline = false;

    public FetchStandingsTask(Context context) {
        this.context = context;
    }

    @Override
    protected ArrayList<StandingsTeam> doInBackground(String... strings) {
        try {
            if (!onlyOffline) {
                String rest_url = context.getString(R.string.rest_interface) + "teams?competition=" + strings[0];
                URL restAddress = new URL(rest_url);
                HttpURLConnection urlConnection = (HttpURLConnection) restAddress.openConnection();
                InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line).append('\n');
                }

                String json = builder.toString();
                Gson gson = new Gson();

                TeamWrapper[] teamsArray = gson.fromJson(json, TeamWrapper[].class);
                ArrayList<TeamWrapper> teamWrappers = new ArrayList<>();
                Collections.addAll(teamWrappers, teamsArray);

                for (TeamWrapper tw : teamWrappers) {
                    if(tw.isActive()){
                        Cacher.cacheTeam(context, tw);
                    }
                    else{
                        Cacher.delete(context, CacheDBHelper.TableColumns.STANDINGSTEAMS_TABLE_NAME, tw.getId());
                    }
                }
            }

            ArrayList<StandingsTeam> teams = new ArrayList<>();

            SQLiteDatabase db = new CacheDBHelper(context).getReadableDatabase();
            Cursor c = db.query(
                    CacheDBHelper.TableColumns.STANDINGSTEAMS_TABLE_NAME,  // The table to query
                    null,                               // The columns to return
                    null,                                // The columns for the WHERE clause
                    null,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    CacheDBHelper.TableColumns.STANDINGSTEAMS_COLUMN_NAME_GROUP + " ASC, wins*3 + ot_wins*2 + ot_losses DESC, gf - ga DESC , gf DESC"                                 // The sort order
            );

            while (c.moveToNext()) {
                teams.add(StandingsTeam.populateStandingsTeam(c));
            }

            return teams;
        } catch (IOException e) {
            e.printStackTrace();
            StandingsTeam[] teamsArray = {};
            ArrayList<StandingsTeam> standingsTeams = new ArrayList<>();
            Collections.addAll(standingsTeams, teamsArray);
            return standingsTeams;
        }
    }

    @Override
    protected void onPostExecute(ArrayList<StandingsTeam> standingsTeams) {
        if (onTeamsFetchedListener != null) {
            onTeamsFetchedListener.onTeamsFetched(standingsTeams);
        }
    }

    public void setOnTeamsFetchedListener(OnTeamsFetchedListener onTeamsFetchedListener) {
        this.onTeamsFetchedListener = onTeamsFetchedListener;
    }

    public boolean isOnlyOffline() {
        return onlyOffline;
    }

    public void setOnlyOffline(boolean onlyOffline) {
        this.onlyOffline = onlyOffline;
    }

    public interface OnTeamsFetchedListener {
        void onTeamsFetched(ArrayList<StandingsTeam> standingsTeams);
    }
}
