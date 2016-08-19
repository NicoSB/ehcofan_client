package com.nicosb.apps.ehcofan.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.nicosb.apps.ehcofan.R;
import com.nicosb.apps.ehcofan.models.StandingsTeam;

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
public class FetchStandingsTask extends AsyncTask<String, Void, ArrayList<StandingsTeam>>{
    Context context;
    OnTeamsFetchedListener onTeamsFetchedListener;

    public FetchStandingsTask(Context context) {
        this.context = context;
    }

    @Override
    protected ArrayList<StandingsTeam> doInBackground(String... strings) {
        try {
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

            StandingsTeam[] teamsArray = gson.fromJson(json, StandingsTeam[].class);
            ArrayList<StandingsTeam> standingsTeams = new ArrayList<>();
            Collections.addAll(standingsTeams, teamsArray);

            return standingsTeams;
        } catch (IOException e) {
            e.printStackTrace();
            StandingsTeam[] teamsArray = {new StandingsTeam("EHC Olten", "NLB 16/17", "", 1,1,1,1,1,1),new StandingsTeam("SC Langenthal", "NLB 16/17", "", 0,1,1,1,1,1)};
            ArrayList<StandingsTeam> standingsTeams = new ArrayList<>();
            Collections.addAll(standingsTeams, teamsArray);
            return standingsTeams;
        }
    }

    @Override
    protected void onPostExecute(ArrayList<StandingsTeam> standingsTeams) {
        if(onTeamsFetchedListener != null){
            onTeamsFetchedListener.onTeamsFetched(standingsTeams);
        }
    }

    public void setOnTeamsFetchedListener(OnTeamsFetchedListener onTeamsFetchedListener) {
        this.onTeamsFetchedListener = onTeamsFetchedListener;
    }

    public interface OnTeamsFetchedListener{
        void onTeamsFetched(ArrayList<StandingsTeam> standingsTeams);
    }
}
