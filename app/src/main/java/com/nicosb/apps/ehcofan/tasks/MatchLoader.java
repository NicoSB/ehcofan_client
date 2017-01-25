package com.nicosb.apps.ehcofan.tasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v4.content.AsyncTaskLoader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nicosb.apps.ehcofan.CacheDBHelper;
import com.nicosb.apps.ehcofan.Cacher;
import com.nicosb.apps.ehcofan.R;
import com.nicosb.apps.ehcofan.models.Match;
import com.nicosb.apps.ehcofan.models.MatchWrapper;
import com.nicosb.apps.ehcofan.retrofit.EHCOFanAPI;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Nico on 25.01.2017.
 */

public class MatchLoader extends AsyncTaskLoader<ArrayList<Match>> {
    private Context context;
    private String competition;
    private EHCOFanAPI mApi;

    public MatchLoader(Context context) {
        super(context);
        this.context = context;
        competition = "";
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder().
                baseUrl(context.getString(R.string.rest_interface))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        mApi = retrofit.create(EHCOFanAPI.class);
    }

    public MatchLoader(Context context, String competition) {
        this(context);
        this.competition = competition;
    }

    @Override
    public ArrayList<Match> loadInBackground() {
        updateMatches();

        ArrayList<Match> matches = new ArrayList<>();
        SQLiteDatabase db = CacheDBHelper.getInstance(context).getReadableDatabase();
        String where = null;

        if (competition.length() > 0) {
            where = CacheDBHelper.TableColumns.MATCHES_COLUMN_NAME_COMPETITION + " = '" + competition + "'";
        }

        Cursor c = db.query(
                CacheDBHelper.TableColumns.MATCHES_TABLE_NAME,  // The table to query
                null,                               // The columns to return
                where,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                "datetime(" + CacheDBHelper.TableColumns.MATCHES_COLUMN_NAME_DATETIME + ")"                                 // The sort order
        );

        while (c.moveToNext()) {
            try {
                Match m = Match.populateMatch(c);
                matches.add(m);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        c.close();
        db.close();

        return matches;
    }

    private void updateMatches(){
        final Callback<ArrayList<MatchWrapper>> matches = new Callback<ArrayList<MatchWrapper>>() {
            @Override
            public void onResponse(Call<ArrayList<MatchWrapper>> call, final Response<ArrayList<MatchWrapper>> response) {
                processWrappers(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<MatchWrapper>> call, Throwable t) {

            }
        };

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String lastUpdated = prefs.getString(context.getString(R.string.pref_match_update), "" );

        Call<ArrayList<MatchWrapper>> call = mApi.listMatches(lastUpdated);
        call.enqueue(matches);
    }
    private void processWrappers(ArrayList<MatchWrapper> wrappers) {
        Calendar lastUpdate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSS" );
        if(wrappers != null) {
            // extract players and save them as well as pictures to local database
            for (MatchWrapper m : wrappers) {
                Match match = m.toMatch();
                if (m.isActive()) {
                    try {
                        Cacher.cacheMatch(context, match);

                        if (lastUpdate == null || lastUpdate.before(sdf.parse(m.getUpdated_at()))) {
                            GregorianCalendar d = new GregorianCalendar();
                            d.setTime(sdf.parse(m.getUpdated_at()));
                            d.add(Calendar.SECOND, 1);
                            lastUpdate = d;
                        }
                    } catch (ParseException pe) {
                        pe.printStackTrace();
                    }
                } else {
                    Cacher.delete(context, CacheDBHelper.TableColumns.MATCHES_TABLE_NAME, m.getId());
                }
            }

            if (lastUpdate != null) {
                // update player update pref
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString((context).getString(R.string.pref_match_update), sdf.format(lastUpdate.getTime()));
                editor.apply();
            }
        }

    }
}
