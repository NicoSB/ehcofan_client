package com.nicosb.apps.ehcofan.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nicosb.apps.ehcofan.CacheDBHelper;
import com.nicosb.apps.ehcofan.FirebaseHandler;
import com.nicosb.apps.ehcofan.R;
import com.nicosb.apps.ehcofan.ToolbarHelper;
import com.nicosb.apps.ehcofan.fragments.ArticleFragment;
import com.nicosb.apps.ehcofan.models.Article;
import com.nicosb.apps.ehcofan.models.Match;
import com.nicosb.apps.ehcofan.models.StandingsTeam;
import com.nicosb.apps.ehcofan.tasks.FetchArticlesTask;
import com.nicosb.apps.ehcofan.tasks.FetchMatchesTask;
import com.nicosb.apps.ehcofan.tasks.FetchStandingsTask;
import com.nicosb.apps.ehcofan.views.ArticleView;
import com.nicosb.apps.ehcofan.views.MatchView;

import java.text.ParseException;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity
        implements FetchArticlesTask.PostExecuteListener,
        FetchMatchesTask.OnScheduleFetchedListener, FetchStandingsTask.OnTeamsFetchedListener {
    private String TAG = "HomeActivity";
    private DrawerLayout drawerLayout;
    private Match mNextMatch;
    private Match mLastMatch;
    private Article mArticle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FirebaseHandler.signIn(this);

        drawerLayout = ToolbarHelper.loadToolbar(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()){
            fetchLatestNews();
        }
        else{
            Toast.makeText(this, "Keine Verbindung zum Internet!", Toast.LENGTH_LONG).show();
        }
        fetchSchedule();
        fetchStandingsTeam();
        drawerLayout.closeDrawer(Gravity.LEFT, false);
    }

    private void fetchStandingsTeam() {
        FetchStandingsTask fetchStandingsTask = new FetchStandingsTask(this);
        fetchStandingsTask.setOnlyOffline(true);
        fetchStandingsTask.setOnTeamsFetchedListener(this);
        fetchStandingsTask.execute(""  );
    }



    private void fetchLatestNews() {
        LinearLayout container = (LinearLayout) findViewById(R.id.container_latest_news);
        container.setVisibility(View.VISIBLE);

        if(mArticle == null) {
            LinearLayout ll_loading = (LinearLayout) findViewById(R.id.container_loading_news);
            ll_loading.setVisibility(View.VISIBLE);
        }

        FetchArticlesTask fetchArticlesTask = new FetchArticlesTask(this);
        fetchArticlesTask.setLimited(true);
        fetchArticlesTask.setPostExecuteListener(this);
        Article[] dummy = new Article[0];
        fetchArticlesTask.execute(dummy);
    }

    private void displayLastMatch() throws ParseException {
        LinearLayout container = (LinearLayout) findViewById(R.id.container_last_match);

        SQLiteDatabase db = new CacheDBHelper(this).getReadableDatabase();
        String where = "datetime(" + CacheDBHelper.TableColumns.MATCHES_COLUMN_NAME_DATETIME + ") < datetime('now') AND (" + CacheDBHelper.TableColumns.MATCHES_COLUMN_NAME_HOME_TEAM +
                " = 'EHC Olten' OR " + CacheDBHelper.TableColumns.MATCHES_COLUMN_NAME_AWAY_TEAM + " = 'EHC Olten')";

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
        if (c.getCount() > 0) {
            c.moveToFirst();
            mLastMatch = Match.populateMatch(c);
            displayMatch(container, mLastMatch, R.id.card_last_match);
        } else {
            FetchMatchesTask fetchMatchesTask = new FetchMatchesTask(this);
            fetchMatchesTask.setOnScheduleFetchedListener(this);
            fetchMatchesTask.execute("" );
        }
        db.close();
        c.close();
    }

    private void displayNextMatch() throws ParseException {
        SQLiteDatabase db = new CacheDBHelper(this).getReadableDatabase();
        String where = "datetime(" + CacheDBHelper.TableColumns.MATCHES_COLUMN_NAME_DATETIME + ") > datetime('now') AND (" + CacheDBHelper.TableColumns.MATCHES_COLUMN_NAME_HOME_TEAM +
                " = 'EHC Olten' OR " + CacheDBHelper.TableColumns.MATCHES_COLUMN_NAME_AWAY_TEAM + " = 'EHC Olten')";

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
        if (c.getCount() > 0) {
            c.moveToFirst();
            mNextMatch = Match.populateMatch(c);
            LinearLayout container = (LinearLayout) findViewById(R.id.container_next_match);
            displayMatch(container, mNextMatch, R.id.card_next_match);
        }
        db.close();
        c.close();
    }

    private void displayMatch(LinearLayout container, Match nextMatch, int card_next_match) {
        container.removeAllViews();

        if(!container.hasOnClickListeners()) {
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent scheduleActivity = new Intent(view.getContext(), ScheduleActivity.class);
                    startActivity(scheduleActivity);
                }
            });
        }

        MatchView mv = new MatchView(this, nextMatch, true);
        container.addView(mv);
        CardView cardView = (CardView) findViewById(card_next_match);
        cardView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseHandler.mAuth != null) {
            FirebaseHandler.mAuth.addAuthStateListener(FirebaseHandler.mAuthListener);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (FirebaseHandler.mAuthListener != null) {
            FirebaseHandler.mAuth.removeAuthStateListener(FirebaseHandler.mAuthListener);
        }
    }

    public void openNewsActivity(View view, Article article) {
        Intent newsActivity = new Intent(this, NewsActivity.class);
        newsActivity.putExtra(ArticleFragment.ARGS_ARTICLE, article);
        startActivity(newsActivity);
    }

    @Override
    public void onPostExecute(ArrayList<Article> articles) {
        if (articles != null && articles.size() > 0) {
            LinearLayout ll_loading = (LinearLayout) findViewById(R.id.container_loading_news);
            ll_loading.setVisibility(View.GONE);
            mArticle = articles.get(0);
            displayArticle();
        }
    }

    private void displayArticle() {
        final ArticleView av = new ArticleView(this, mArticle);
        av.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNewsActivity(view, av.getArticle());
            }
        });

        LinearLayout container = (LinearLayout) findViewById(R.id.container_latest_news);
        container.addView(av);

        CardView cardView = (CardView) findViewById(R.id.card_latest_news);
        cardView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onScheduleFetched(ArrayList<Match> matches) {
        try {
            if (matches.size() > 0) {
                displayNextMatch();
                displayLastMatch();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTeamsFetched(ArrayList<StandingsTeam> standingsTeams) {
        int rank = 0;
        for (StandingsTeam st : standingsTeams) {
            rank++;
            if (st.getName().equals("EHC Olten" )) {
                fillNLBCard(st, rank);
                return;
            }
        }
    }

    private void fillNLBCard(StandingsTeam st, int rank) {
        TextView txt_rank = (TextView) findViewById(R.id.txt_team_placement);
        TextView txt_games = (TextView) findViewById(R.id.txt_team_games);
        TextView txt_points = (TextView) findViewById(R.id.txt_team_points);
        TextView txt_goals = (TextView) findViewById(R.id.txt_team_goals);

        txt_rank.setText(String.valueOf(rank));
        txt_games.setText(String.valueOf(st.getGames()));
        txt_points.setText(String.valueOf(st.getPoints()));
        txt_goals.setText(String.valueOf(st.getGoals_for() - st.getGoals_against()));
    }

    public void startStandingsActivity(View view) {
        Intent standingsActivity = new Intent(this, StandingsActivity.class);
        startActivity(standingsActivity);
    }


    private void fetchSchedule() {
        FetchMatchesTask fetchMatchesTask = new FetchMatchesTask(this);
        fetchMatchesTask.setOnScheduleFetchedListener(this);
        fetchMatchesTask.execute("" );
    }

    private void fetchTeams() {
        FetchStandingsTask fetchStandingsTask = new FetchStandingsTask(this);
        fetchStandingsTask.setOnTeamsFetchedListener(this);
        fetchStandingsTask.execute("NLB%2016/17" );
    }
}
