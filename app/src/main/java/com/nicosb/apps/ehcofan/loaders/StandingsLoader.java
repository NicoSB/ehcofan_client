package com.nicosb.apps.ehcofan.loaders;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.AsyncTaskLoader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nicosb.apps.ehcofan.CacheDBHelper;
import com.nicosb.apps.ehcofan.Cacher;
import com.nicosb.apps.ehcofan.R;
import com.nicosb.apps.ehcofan.models.StandingsTeam;
import com.nicosb.apps.ehcofan.models.TeamWrapper;
import com.nicosb.apps.ehcofan.retrofit.EHCOFanAPI;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Nico on 30.01.2017.
 */

public class StandingsLoader extends AsyncTaskLoader<ArrayList<StandingsTeam>> implements Callback<ArrayList<TeamWrapper>> {
    private final EHCOFanAPI mApi;
    private Context context;
    private String competition;

    public StandingsLoader(Context context){
        this(context, "");
    }

    public StandingsLoader(Context context, String competition) {
        super(context);
        this.context = context;
        this.competition = competition;

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder().
                baseUrl(context.getString(R.string.rest_interface))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        mApi = retrofit.create(EHCOFanAPI.class);
    }

    @Override
    public ArrayList<StandingsTeam> loadInBackground() {
        updateStandings();

        ArrayList<StandingsTeam> teams = new ArrayList<>();

        SQLiteDatabase db = CacheDBHelper.getReadableDB(context);
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
        c.close();

        return teams;
    }

    private void updateStandings() {
        Call<ArrayList<TeamWrapper>> matches;
        if(competition.equals(""))  matches = mApi.listTeams();
        else    matches = mApi.listTeams(competition);

        matches.enqueue(this);
    }


    private void processWrappers(ArrayList<TeamWrapper> wrappers){
        for (TeamWrapper tw : wrappers) {
            if(tw.isActive()){
                Cacher.cacheTeam(context, tw);
            }
            else{
                Cacher.delete(context, CacheDBHelper.TableColumns.STANDINGSTEAMS_TABLE_NAME, tw.getId());
            }
        }
    }

    @Override
    public void onResponse(Call<ArrayList<TeamWrapper>> call, Response<ArrayList<TeamWrapper>> response) {
        processWrappers(response.body());
    }

    @Override
    public void onFailure(Call<ArrayList<TeamWrapper>> call, Throwable t) {

    }
}
