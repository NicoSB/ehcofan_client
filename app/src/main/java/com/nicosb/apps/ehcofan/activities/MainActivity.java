package com.nicosb.apps.ehcofan.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nicosb.apps.ehcofan.BuildConfig;
import com.nicosb.apps.ehcofan.CacheDBHelper;
import com.nicosb.apps.ehcofan.FirebaseHandler;
import com.nicosb.apps.ehcofan.R;
import com.nicosb.apps.ehcofan.models.StandingsTeam;
import com.nicosb.apps.ehcofan.loaders.MatchLoader;
import com.nicosb.apps.ehcofan.loaders.PlayerLoader;
import com.nicosb.apps.ehcofan.loaders.StandingsLoader;
import com.nicosb.apps.ehcofan.retrofit.EHCOFanAPI;

import java.io.IOException;
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
 * Created by Nico on 17.09.2016.
 */
public class MainActivity extends AppCompatActivity{

    private ProgressBar mProgress;
    private int mProgressStatus = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        checkVersion();

        mProgress = (ProgressBar) findViewById(R.id.main_progressbar);
        mProgressStatus = 0;
        String lastDumped = prefs.getString(getString(R.string.pref_db_dump), "" );
        Calendar now = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if(lastDumped.length() > 0){
            GregorianCalendar gc = new GregorianCalendar();
            try {
                gc.setTime(sdf.parse(lastDumped));
                int datediff = now.get(Calendar.DAY_OF_YEAR) - gc.get(Calendar.DAY_OF_YEAR);
                if(networkInfo != null && (networkInfo.getTypeName().equals("WIFI") && datediff >= 3 ) || datediff >= 14 || now.get(Calendar.YEAR) > gc.get(Calendar.YEAR)){
                    flushTables();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else{
            flushTables();
        }
        if (networkInfo != null && networkInfo.isConnected()) {
            fetchTeams();
            fetchSchedule();
            fetchPlayers();
            fetchIsPO();
        } else {
            Toast.makeText(this, "Keine Verbindung zum Internet", Toast.LENGTH_SHORT).show();
            updateProgressStatus(100);
        }
    }

    private void checkVersion() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int storedVersion = prefs.getInt(getString(R.string.pref_version), 0);

        if(storedVersion < BuildConfig.VERSION_CODE){
            flushTables();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(getString(R.string.pref_version), BuildConfig.VERSION_CODE).apply();
            try {
                FirebaseInstanceId.getInstance().deleteInstanceId();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void flushTables() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar now = Calendar.getInstance();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        SQLiteDatabase db = CacheDBHelper.getInstance(this).getReadableDatabase();
        CacheDBHelper.truncateTables(db);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(getString(R.string.pref_player_update), "");
        editor.putString(getString(R.string.pref_match_update), "");
        editor.putString(getString(R.string.pref_db_dump), sdf.format(now.getTime()));
        editor.apply();
    }

    private void fetchPlayers() {
        final int loaderId = 110;

        getSupportLoaderManager().initLoader(loaderId, getIntent().getExtras(), new LoaderManager.LoaderCallbacks() {
            @Override
            public Loader onCreateLoader(int id, Bundle args) {
                return new PlayerLoader(MainActivity.this);
            }

            @Override
            public void onLoadFinished(Loader loader, Object data) {
                updateProgressStatus(25);
                getSupportLoaderManager().destroyLoader(loaderId);
            }

            @Override
            public void onLoaderReset(Loader loader) {

            }
        }).forceLoad();
    }

    private void fetchSchedule() {
        final int loaderId = 100;
        getSupportLoaderManager().initLoader(loaderId, getIntent().getExtras(), new LoaderManager.LoaderCallbacks()
        {
            @Override
            public Loader onCreateLoader(int id, Bundle args) {
                return new MatchLoader(MainActivity.this);
            }

            @Override
            public void onLoadFinished(Loader loader, Object data) {
                updateProgressStatus(25);
                getSupportLoaderManager().destroyLoader(loaderId);
            }

            @Override
            public void onLoaderReset(Loader loader) {}
        }).forceLoad();
    }

    private void fetchTeams() {
        final int STANDINGS_LOADER_ID = 1933;
        getSupportLoaderManager().initLoader(STANDINGS_LOADER_ID, getIntent().getExtras(), new LoaderManager.LoaderCallbacks<ArrayList<StandingsTeam>>() {
            @Override
            public Loader<ArrayList<StandingsTeam>> onCreateLoader(int id, Bundle args) {
                return new StandingsLoader(MainActivity.this, "NLB%2016/17");
            }

            @Override
            public void onLoadFinished(Loader<ArrayList<StandingsTeam>> loader, ArrayList<StandingsTeam> data) {
                updateProgressStatus(25);
                getSupportLoaderManager().destroyLoader(STANDINGS_LOADER_ID);
            }

            @Override
            public void onLoaderReset(Loader<ArrayList<StandingsTeam>> loader) {

            }
        }).forceLoad();
    }

    private void fetchIsPO(){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder().
                baseUrl(getString(R.string.rest_interface))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        EHCOFanAPI mApi = retrofit.create(EHCOFanAPI.class);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        final SharedPreferences.Editor editor = prefs.edit();

        Callback<Boolean> isPlayoffs = new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                editor.putBoolean(getString(R.string.pref_playoff), response.body());
                editor.apply();
                updateProgressStatus(25);
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                editor.putBoolean(getString(R.string.pref_playoff), false);
                editor.apply();
                updateProgressStatus(25);
            }
        };
        Call<Boolean> po = mApi.isPlayoffs();
        po.enqueue(isPlayoffs);
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
