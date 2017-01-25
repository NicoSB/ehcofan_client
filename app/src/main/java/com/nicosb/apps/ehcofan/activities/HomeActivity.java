package com.nicosb.apps.ehcofan.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nicosb.apps.ehcofan.CacheDBHelper;
import com.nicosb.apps.ehcofan.FirebaseHandler;
import com.nicosb.apps.ehcofan.R;
import com.nicosb.apps.ehcofan.ToolbarHelper;
import com.nicosb.apps.ehcofan.fragments.ArticleFragment;
import com.nicosb.apps.ehcofan.models.Article;
import com.nicosb.apps.ehcofan.models.ArticleWrapper;
import com.nicosb.apps.ehcofan.models.Match;
import com.nicosb.apps.ehcofan.models.MatchWrapper;
import com.nicosb.apps.ehcofan.models.POMatchupWrapper;
import com.nicosb.apps.ehcofan.models.StandingsTeam;
import com.nicosb.apps.ehcofan.retrofit.EHCOFanAPI;
import com.nicosb.apps.ehcofan.tasks.ArticleImageLoader;
import com.nicosb.apps.ehcofan.tasks.FetchStandingsTask;
import com.nicosb.apps.ehcofan.tasks.MatchLoader;
import com.nicosb.apps.ehcofan.tasks.PlayoffMatchupLoader;
import com.nicosb.apps.ehcofan.views.ArticleView;
import com.nicosb.apps.ehcofan.views.MatchView;
import com.nicosb.apps.ehcofan.views.PlayoffView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import retrofit2.converter.gson.GsonConverterFactory;

public class HomeActivity extends AppCompatActivity
        implements FetchStandingsTask.OnTeamsFetchedListener, Callback<List<MatchWrapper>> {
    private String TAG = "HomeActivity";
    private DrawerLayout drawerLayout;
    private boolean showPB = true;
    private boolean isPlayoff = false;
    public static final String PREF_IS_PO = "po";
    private PlayoffView view_po;
    private EHCOFanAPI mApi;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FirebaseHandler.signIn(this);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        isPlayoff = prefs.getBoolean(getString(R.string.pref_playoff), false);
        if(isPlayoff){
            fetchPlayoffs(savedInstanceState);
        }
        else
        {
            CardView cv_lastMatch = (CardView)findViewById(R.id.card_last_match);
            CardView cv_nextMatch = (CardView)findViewById(R.id.card_next_match);
            CardView nlb = (CardView)findViewById(R.id.card_nlb);

            cv_lastMatch.setVisibility(View.VISIBLE);
            cv_nextMatch.setVisibility(View.VISIBLE);
            nlb.setVisibility(View.VISIBLE);
        }

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder().
                baseUrl(getString(R.string.rest_interface))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        mApi = retrofit.create(EHCOFanAPI.class);
        drawerLayout = ToolbarHelper.loadToolbar(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()){
            fetchLatestNews(getIntent().getExtras());
        }
        else{
            Toast.makeText(this, "Keine Verbindung zum Internet!", Toast.LENGTH_LONG).show();
        }
        fetchSchedule();
        if(!isPlayoff) {
            fetchStandingsTeam();
        }
        drawerLayout.closeDrawer(GravityCompat.START, false);
    }

    private void fetchPlayoffs(Bundle bundle) {
        getSupportLoaderManager().initLoader(2, bundle, new LoaderManager.LoaderCallbacks<POMatchupWrapper>() {

            @Override
            public Loader<POMatchupWrapper> onCreateLoader(int id, Bundle args) {
                return new PlayoffMatchupLoader(HomeActivity.this);
            }

            @Override
            public void onLoadFinished(Loader<POMatchupWrapper> loader, POMatchupWrapper data) {
                CardView cv_PO = (CardView)findViewById(R.id.card_playoffs);
                cv_PO.setVisibility(View.VISIBLE);

                LinearLayout container_playoff = (LinearLayout)findViewById(R.id.container_playoff);

                view_po = new PlayoffView(HomeActivity.this, data.toMatchup(HomeActivity.this));
                container_playoff.addView(view_po);
                getSupportLoaderManager().destroyLoader(2);
            }

            @Override
            public void onLoaderReset(Loader<POMatchupWrapper> loader) {

            }
        }).forceLoad();
    }

    private void fetchStandingsTeam() {
        FetchStandingsTask fetchStandingsTask = new FetchStandingsTask(this);
        fetchStandingsTask.setOnlyOffline(true);
        fetchStandingsTask.setOnTeamsFetchedListener(this);
        fetchStandingsTask.execute(""  );
    }

    private void fetchLatestNews(final Bundle savedInstanceState) {
        LinearLayout container = (LinearLayout) findViewById(R.id.container_latest_news);
        container.setVisibility(View.VISIBLE);

        if(showPB) {
            LinearLayout ll_loading = (LinearLayout) findViewById(R.id.container_loading_news);
            ll_loading.setVisibility(View.VISIBLE);
        }

        showPB = false;
        final Callback<ArrayList<ArticleWrapper>> articles = new Callback<ArrayList<ArticleWrapper>>() {
            @Override
            public void onResponse(Call<ArrayList<ArticleWrapper>> call, final Response<ArrayList<ArticleWrapper>> response) {
                getSupportLoaderManager().initLoader(2, savedInstanceState, new LoaderManager.LoaderCallbacks<ArrayList<Article>>() {
                    @Override
                    public Loader<ArrayList<Article>> onCreateLoader(int id, Bundle args) {
                        return new ArticleImageLoader(HomeActivity.this, (Article[])null, response.body());
                    }

                    @Override
                    public void onLoadFinished(Loader<ArrayList<Article>> loader, ArrayList<Article> data) {
                        LinearLayout ll_loading = (LinearLayout) findViewById(R.id.container_loading_news);
                        ll_loading.setVisibility(View.GONE);
                        displayArticle(data.get(0));
                    }

                    @Override
                    public void onLoaderReset(Loader<ArrayList<Article>> loader) {

                    }
                }).forceLoad();
            }

            @Override
            public void onFailure(Call<ArrayList<ArticleWrapper>> call, Throwable t) {

            }
        };

        Call<ArrayList<ArticleWrapper>> call = mApi.listArticles(1);
        call.enqueue(articles);
    }

    private void displayLastMatch() throws ParseException {
        LinearLayout container = (LinearLayout) findViewById(R.id.container_last_match);

        SQLiteDatabase db = CacheDBHelper.getInstance(this).getReadableDatabase();
        String where = CacheDBHelper.TableColumns.MATCHES_COLUMN_NAME_STATUS + " LIKE '%Ende%'";

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
            Match match = Match.populateMatch(c);
            displayMatch(container, match);
        } else {
            fetchSchedule();
        }
        db.close();
        c.close();
    }

    private void displayNextMatch() throws ParseException {
        SQLiteDatabase db = CacheDBHelper.getInstance(this).getReadableDatabase();
        String where = CacheDBHelper.TableColumns.MATCHES_COLUMN_NAME_STATUS + " NOT LIKE '%ENDE%' OR " + CacheDBHelper.TableColumns.MATCHES_COLUMN_NAME_STATUS + " IS NULL";
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
            Match match = Match.populateMatch(c);
            LinearLayout container = (LinearLayout) findViewById(R.id.container_next_match);
            displayMatch(container, match);
        }
        db.close();
        c.close();
    }

    private void displayMatch(LinearLayout container, Match match) {
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

        MatchView mv = new MatchView(this, match, true);
        container.addView(mv);
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

    private void openNewsActivity(View view, Article article) {
        Intent newsActivity = new Intent(this, NewsActivity.class);
        newsActivity.putExtra(ArticleFragment.ARGS_ARTICLE, article);
        startActivity(newsActivity);
    }

    private void displayArticle(Article a) {
        final ArticleView av = new ArticleView(this, a);

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
        getSupportLoaderManager().initLoader(1, getIntent().getExtras(), new LoaderManager.LoaderCallbacks<ArrayList<Match>>() {
            @Override
            public Loader<ArrayList<Match>> onCreateLoader(int id, Bundle args) {
                return new MatchLoader(HomeActivity.this);
            }

            @Override
            public void onLoadFinished(Loader<ArrayList<Match>> loader, ArrayList<Match> data) {
                try {
                    displayNextMatch();
                    displayLastMatch();
                }catch (ParseException pe){
                    pe.printStackTrace();
                }
            }

            @Override
            public void onLoaderReset(Loader<ArrayList<Match>> loader) {

            }
        }).forceLoad();
    }



    public void switchGame(View view){
        view_po.changeGame(view);
    }

    // RETROFIT
    @Override
    public void onResponse(Call<List<MatchWrapper>> call, Response<List<MatchWrapper>> response) {
            for(MatchWrapper mw: response.body()){
                Match m = mw.toMatch();
                Log.d(TAG, m.getHome_team());
            }
    }

    @Override
    public void onFailure(Call<List<MatchWrapper>> call, Throwable t) {

    }
}
