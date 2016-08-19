package com.nicosb.apps.ehcofan.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.nicosb.apps.ehcofan.FirebaseHandler;
import com.nicosb.apps.ehcofan.MatchCacheHelper;
import com.nicosb.apps.ehcofan.R;
import com.nicosb.apps.ehcofan.models.Match;
import com.nicosb.apps.ehcofan.views.MatchView;

import java.text.ParseException;

public class MainActivity extends AppCompatActivity {
    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FirebaseHandler.signIn(this);

        try {
            displayLastMatch();
            displayNextMatch();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void displayLastMatch() throws ParseException {
        LinearLayout container = (LinearLayout)findViewById(R.id.container_last_match);

        SQLiteDatabase db = new MatchCacheHelper(this).getReadableDatabase();
        String where = "datetime(" + MatchCacheHelper.MatchCache.COLUMN_NAME_DATETIME + ") < datetime('now')";

        Cursor c = db.query(
                MatchCacheHelper.MatchCache.TABLE_NAME,  // The table to query
                null,                               // The columns to return
                where,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                "datetime(" + MatchCacheHelper.MatchCache.COLUMN_NAME_DATETIME + ")", // The sort order
                "1"                                   // LIMIT
        );
        if(c.getColumnCount() > 0) {
            c.moveToFirst();
            Match lastMatch = Match.populateMatch(c);
            MatchView mv = new MatchView(this, lastMatch, true);
            container.addView(mv);
        }
        c.close();
    }

    private void displayNextMatch() throws ParseException {
        LinearLayout container = (LinearLayout)findViewById(R.id.container_next_match);

        SQLiteDatabase db = new MatchCacheHelper(this).getReadableDatabase();
        String where = "datetime(" + MatchCacheHelper.MatchCache.COLUMN_NAME_DATETIME + ") > datetime('now')";

        Cursor c = db.query(
                MatchCacheHelper.MatchCache.TABLE_NAME,  // The table to query
                null,                               // The columns to return
                where,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                "datetime(" + MatchCacheHelper.MatchCache.COLUMN_NAME_DATETIME + ")", // The sort order
                "1"                                   // LIMIT
        );
        if(c.getColumnCount() > 0) {
            c.moveToFirst();
            Match nextMatch = Match.populateMatch(c);
            MatchView mv = new MatchView(this, nextMatch, true);
            container.addView(mv);
        }
        c.close();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseHandler.mAuth != null){
            FirebaseHandler.mAuth.addAuthStateListener(FirebaseHandler.mAuthListener);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(FirebaseHandler.mAuthListener != null){
            FirebaseHandler.mAuth.removeAuthStateListener(FirebaseHandler.mAuthListener);
        }
    }
    public void openNewsActivity(View view) {
        Intent newsActivity = new Intent(this, NewsActivity.class);
        startActivity(newsActivity);
    }

    public void openScheduleActivity(View view) {
        Intent scheduleActivity = new Intent(this, ScheduleActivity.class);
        startActivity(scheduleActivity);
    }

    public void openRosterActivity(View view) {
        Intent scheduleActivity = new Intent(this, RosterActivity.class);
        startActivity(scheduleActivity);
    }

    public void openStandingsActivity(View view) {
        Intent standingsActivity = new Intent(this, StandingsActivity.class);
        startActivity(standingsActivity);
    }

    public void openCupActivity(View view){
        Intent cupActivity = new Intent(this, CupActivity.class);
        startActivity(cupActivity);
    }
}
