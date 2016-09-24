package com.nicosb.apps.ehcofan.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;

import com.nicosb.apps.ehcofan.R;
import com.nicosb.apps.ehcofan.ToolbarHelper;
import com.nicosb.apps.ehcofan.fragments.StandingsFragment;

/**
 * Created by Nico on 22.07.2016.
 */
public class StandingsActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standings);

        StandingsFragment standingsFragment = new StandingsFragment();
        Bundle args = new Bundle();
        args.putString(StandingsFragment.KEY_COMPETITION, "NLB%2016/17" );
        standingsFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.standings_content_frame, standingsFragment, "Tabelle" ).commit();

        drawerLayout = ToolbarHelper.loadToolbar(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        drawerLayout.closeDrawer(GravityCompat.START, false);
    }
}
