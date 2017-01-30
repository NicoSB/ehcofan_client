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

import com.nicosb.apps.ehcofan.CacheDBHelper;
import com.nicosb.apps.ehcofan.R;
import com.nicosb.apps.ehcofan.models.StandingsTeam;
import com.nicosb.apps.ehcofan.tasks.FetchStandingsTask;
import com.nicosb.apps.ehcofan.tasks.IsPlayoffLoader;
import com.nicosb.apps.ehcofan.tasks.MatchLoader;
import com.nicosb.apps.ehcofan.tasks.PlayerLoader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Nico on 17.09.2016.
 */
public class MainActivity extends AppCompatActivity
        implements FetchStandingsTask.OnTeamsFetchedListener{

    private ProgressBar mProgress;
    private int mProgressStatus = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

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
                    flushTables(prefs, sdf, now);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else{
            flushTables(prefs, sdf, now);
        }
        if (networkInfo != null && networkInfo.isConnected()) {
            fetchTeams();
            fetchSchedule();
            fetchPlayers();
            fetchIsPO(savedInstanceState);
        } else {
            Toast.makeText(this, "Keine Verbindung zum Internet", Toast.LENGTH_SHORT).show();
            updateProgressStatus(100);
        }
    }

    private void flushTables(SharedPreferences prefs, SimpleDateFormat sdf, Calendar now) {
        SQLiteDatabase db = CacheDBHelper.getInstance(this).getReadableDatabase();
        CacheDBHelper.truncateTables(db);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(getString(R.string.pref_player_update), "");
        editor.putString(getString(R.string.pref_match_update), "");
        editor.putString(getString(R.string.pref_db_dump), sdf.format(now.getTime()));
        editor.apply();
        db.close();
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
        FetchStandingsTask fetchStandingsTask = new FetchStandingsTask(this);
        fetchStandingsTask.setOnTeamsFetchedListener(this);
        fetchStandingsTask.execute("NLB%2016/17" );
    }

    private void fetchIsPO(Bundle bundle){
        final int loaderId = 101;
        getSupportLoaderManager().initLoader(loaderId, bundle, new LoaderManager.LoaderCallbacks<Void>() {
            @Override
            public Loader<Void> onCreateLoader(int id, Bundle args) {
                return new IsPlayoffLoader(MainActivity.this);
            }

            @Override
            public void onLoadFinished(Loader<Void> loader, Void data) {
                updateProgressStatus(25);
                getSupportLoaderManager().destroyLoader(loaderId);
            }

            @Override
            public void onLoaderReset(Loader<Void> loader) {

            }
        }).forceLoad();
    }

    @Override
    public void onTeamsFetched(ArrayList<StandingsTeam> standingsTeams) {
        updateProgressStatus(25);
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
