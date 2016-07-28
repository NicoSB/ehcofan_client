package com.nicosb.apps.ehcofan.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.nicosb.apps.ehcofan.R;
import com.nicosb.apps.ehcofan.models.Match;
import com.nicosb.apps.ehcofan.models.Player;
import com.nicosb.apps.ehcofan.models.Team;

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
public class FetchStandingsTask extends AsyncTask<String, Void, ArrayList<Team>>{
    Context context;
    OnTeamsFetchedListener onTeamsFetchedListener;

    public FetchStandingsTask(Context context) {
        this.context = context;
    }

    @Override
    protected ArrayList<Team> doInBackground(String... strings) {
        try {
            String rest_url = context.getString(R.string.rest_interface) + "teams?competition=NLB%2016/17";
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

            Team[] teamsArray = gson.fromJson(json, Team[].class);
            ArrayList<Team> teams = new ArrayList<>();
            Collections.addAll(teams, teamsArray);

            return teams;
        } catch (IOException e) {
            e.printStackTrace();
            Team[] teamsArray = {new Team("EHC Olten", "NLB 16/17", 1,1,1,1,1,1),new Team("SC Langenthal", "NLB 16/17", 0,1,1,1,1,1)};
            ArrayList<Team> teams = new ArrayList<>();
            Collections.addAll(teams, teamsArray);
            return teams;
        }
    }

    @Override
    protected void onPostExecute(ArrayList<Team> teams) {
        if(onTeamsFetchedListener != null){
            onTeamsFetchedListener.onTeamsFetched(teams);
        }
    }

    public void setOnTeamsFetchedListener(OnTeamsFetchedListener onTeamsFetchedListener) {
        this.onTeamsFetchedListener = onTeamsFetchedListener;
    }

    public interface OnTeamsFetchedListener{
        void onTeamsFetched(ArrayList<Team> teams);
    }
}
