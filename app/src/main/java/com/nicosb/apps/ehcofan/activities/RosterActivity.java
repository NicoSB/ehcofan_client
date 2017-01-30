package com.nicosb.apps.ehcofan.activities;

import android.app.DialogFragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nicosb.apps.ehcofan.R;
import com.nicosb.apps.ehcofan.ToolbarHelper;
import com.nicosb.apps.ehcofan.fragments.PlayerInfoFragment;
import com.nicosb.apps.ehcofan.models.Player;
import com.nicosb.apps.ehcofan.loaders.PlayerLoader;
import com.nicosb.apps.ehcofan.views.PlayerView;

import java.util.ArrayList;

/**
 * Created by Nico on 22.07.2016.
 */
public class RosterActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roster);

        final int loaderId = 30;
        getSupportLoaderManager().initLoader(loaderId, savedInstanceState, new LoaderManager.LoaderCallbacks<ArrayList<Player>>(){

            @Override
            public Loader<ArrayList<Player>> onCreateLoader(int id, Bundle args) {
                return new PlayerLoader(RosterActivity.this);
            }

            @Override
            public void onLoadFinished(Loader<ArrayList<Player>> loader, ArrayList<Player> data) {
                processPlayers(data);
                getSupportLoaderManager().destroyLoader(loaderId);
            }

            @Override
            public void onLoaderReset(Loader<ArrayList<Player>> loader) {

            }
        }).forceLoad();

        progressBar = new ProgressBar(this);
        LinearLayout container = (LinearLayout) findViewById(R.id.container_roster);
        container.addView(progressBar);

        drawerLayout = ToolbarHelper.loadToolbar(this);
    }

    private void processPlayers(ArrayList<Player> players) {
        LinearLayout container = (LinearLayout) findViewById(R.id.container_roster);
        String position = "";
        for (final Player p : players) {
            PlayerView playerView = new PlayerView(this, p);
            playerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogFragment playerInfo = new PlayerInfoFragment();
                    Bundle args = new Bundle();
                    args.putParcelable("player", p);
                    playerInfo.setArguments(args);
                    playerInfo.show(getFragmentManager(), "player" );
                }
            });

            if (p.getPosition() != null && !p.getPosition().equals(position)) {
                position = p.getPosition();

                TextView tv = new TextView(this);
                tv.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                tv.setBackgroundColor(getResources().getColor(R.color.mainGreen));
                tv.setTextColor(getResources().getColor(R.color.white));
                tv.setTypeface(Typeface.DEFAULT_BOLD);
                tv.setText(position);

                float scale = getResources().getDisplayMetrics().density;
                int padding = (int) (8 * scale + 0.5f);

                tv.setPadding(padding, padding, padding, padding);

                container.addView(tv);
            }
            container.addView(playerView);
        }
        container.removeView(progressBar);
    }

    @Override
    protected void onStop() {
        super.onStop();
        drawerLayout.closeDrawer(GravityCompat.START, false);
    }
}
