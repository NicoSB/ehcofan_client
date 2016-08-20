package com.nicosb.apps.ehcofan.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.nicosb.apps.ehcofan.CacheDBHelper;
import com.nicosb.apps.ehcofan.FirebaseHandler;
import com.nicosb.apps.ehcofan.R;
import com.nicosb.apps.ehcofan.fragments.ArticleFragment;
import com.nicosb.apps.ehcofan.models.Article;
import com.nicosb.apps.ehcofan.models.Match;
import com.nicosb.apps.ehcofan.tasks.FetchArticlesTask;
import com.nicosb.apps.ehcofan.tasks.FetchMatchesTask;
import com.nicosb.apps.ehcofan.views.ArticleView;
import com.nicosb.apps.ehcofan.views.MatchView;

import java.text.ParseException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
                            implements FetchArticlesTask.PostExecuteListener,
                                FetchMatchesTask.OnScheduleFetchedListener{
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

            ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if(networkInfo != null && networkInfo.isConnected()) {
                displayLatestNews();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void displayLatestNews() {
        FetchArticlesTask fetchArticlesTask = new FetchArticlesTask(this);
        fetchArticlesTask.setLimited(true);
        fetchArticlesTask.setPostExecuteListener(this);
        Article[] dummy = new Article[0];
        fetchArticlesTask.execute(dummy);
    }

    private void displayLastMatch() throws ParseException {
        LinearLayout container = (LinearLayout)findViewById(R.id.container_last_match);

        SQLiteDatabase db = new CacheDBHelper(this).getReadableDatabase();
        String where = "datetime(" + CacheDBHelper.TableColumns.MATCHES_COLUMN_NAME_DATETIME + ") < datetime('now') AND (" + CacheDBHelper.TableColumns.MATCHES_COLUMN_NAME_HOME_TEAM +
                " = 'EHC Olten' OR " +  CacheDBHelper.TableColumns.MATCHES_COLUMN_NAME_AWAY_TEAM + " = 'EHC Olten')";

        Cursor c = db.query(
                CacheDBHelper.TableColumns.MATCHES_TABLE_NAME,  // The table to query
                null,                               // The columns to return
                where,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                "datetime(" + CacheDBHelper.TableColumns.MATCHES_COLUMN_NAME_DATETIME + ") DESC", // The sort order
                "1"                                   // LIMIT
        );
        if(c.getCount() > 0) {
            c.moveToFirst();
            Match lastMatch = Match.populateMatch(c);
            MatchView mv = new MatchView(this, lastMatch, true);
            container.addView(mv);
            CardView cardView = (CardView)findViewById(R.id.card_last_match);
            cardView.setVisibility(View.VISIBLE);
        }
        else{
            FetchMatchesTask fetchMatchesTask = new FetchMatchesTask(this);
            fetchMatchesTask.setOnScheduleFetchedListener(this);
            fetchMatchesTask.execute("");
        }
        db.close();
        c.close();
    }

    private void displayNextMatch() throws ParseException {
        LinearLayout container = (LinearLayout)findViewById(R.id.container_next_match);

        SQLiteDatabase db = new CacheDBHelper(this).getReadableDatabase();
        String where = "datetime(" + CacheDBHelper.TableColumns.MATCHES_COLUMN_NAME_DATETIME + ") > datetime('now') AND (" + CacheDBHelper.TableColumns.MATCHES_COLUMN_NAME_HOME_TEAM +
                " = 'EHC Olten' OR " +  CacheDBHelper.TableColumns.MATCHES_COLUMN_NAME_AWAY_TEAM + " = 'EHC Olten')";

        Cursor c = db.query(
                CacheDBHelper.TableColumns.MATCHES_TABLE_NAME,  // The table to query
                null,                               // The columns to return
                where,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                "datetime(" + CacheDBHelper.TableColumns.MATCHES_COLUMN_NAME_DATETIME + ")", // The sort order
                "1"                                   // LIMIT
        );
        if(c.getCount() > 0) {
            c.moveToFirst();
            Match nextMatch = Match.populateMatch(c);
            MatchView mv = new MatchView(this, nextMatch, true);
            container.addView(mv);
            CardView cardView = (CardView)findViewById(R.id.card_next_match);
            cardView.setVisibility(View.VISIBLE);
        }
        db.close();
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

    public void openNewsActivity(View view, Article article) {
        Intent newsActivity = new Intent(this, NewsActivity.class);
        newsActivity.putExtra(ArticleFragment.ARGS_ARTICLE, article);
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

    @Override
    public void onPostExecute(ArrayList<Article> articles) {
        LinearLayout container = (LinearLayout)findViewById(R.id.container_latest_news);
        final ArticleView av = new ArticleView(this, articles.get(0));
        av.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNewsActivity(view, av.getArticle());
            }
        });
        container.addView(av);

        CardView cardView = (CardView)findViewById(R.id.card_latest_news);
        cardView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onScheduleFetched(ArrayList<Match> matches){
        try {
            if(matches.size() > 0){
                displayNextMatch();
                displayLastMatch();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
