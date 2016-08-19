package com.nicosb.apps.ehcofan.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.nicosb.apps.ehcofan.PremadeDBHelper;
import com.nicosb.apps.ehcofan.R;
import com.nicosb.apps.ehcofan.ToolbarHelper;
import com.nicosb.apps.ehcofan.fragments.CupTeamsFragment;
import com.nicosb.apps.ehcofan.fragments.ScheduleFragment;
import com.nicosb.apps.ehcofan.fragments.StandingsFragment;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nico on 14.08.2016.
 */
public class CupActivity extends AppCompatActivity {
    private TabAdapter mAdapter;
    private ViewPager mPager;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cup);

        mAdapter = new TabAdapter(getSupportFragmentManager());

        mPager = (ViewPager) findViewById(R.id.cup_pager);
        setupAdapter();

        TabLayout tabLayout = (TabLayout) findViewById(R.id.cup_tl);
        tabLayout.setupWithViewPager(mPager);

        mDrawerLayout = ToolbarHelper.loadToolbar(this);
    }

    private void setupAdapter() {
        mAdapter.addFragment(new CupTeamsFragment(), "Teams");

        ScheduleFragment sf = new ScheduleFragment();
        sf.setInPager(true);
        mAdapter.addFragment(sf, "Spielplan");

        StandingsFragment standingsFragment = new StandingsFragment();
        Bundle args = new Bundle();
        args.putString(StandingsFragment.KEY_COMPETITION, "EHCO%20Cup%202016");
        standingsFragment.setArguments(args);
        standingsFragment.setInPager(true);
        mAdapter.addFragment(standingsFragment, "Tabelle");

        mPager.setAdapter(mAdapter);
    }

    public void openTeamFragment(View view) {
        Intent teamActivity = new Intent(this, TeamActivity.class);
        switch(view.getId()){
            case R.id.logo_ehco:
                teamActivity.putExtra("team_id", 1);
                break;
            case R.id.logo_gs:
                teamActivity.putExtra("team_id", 5);
                break;
            case R.id.logo_gw:
                teamActivity.putExtra("team_id", 4);
                break;
            case R.id.logo_ir:
                teamActivity.putExtra("team_id", 3);
                break;
            case R.id.logo_kp:
                teamActivity.putExtra("team_id", 6);
                break;
            case R.id.logo_scl:
                teamActivity.putExtra("team_id", 2);
                break;
        }
        startActivity(teamActivity);
    }

    @Override
    public void onStop() {
        super.onStop();
        mDrawerLayout.closeDrawer(GravityCompat.START, false);
    }

    static class TabAdapter extends FragmentPagerAdapter {
        private static final int NUM_PAGES = 3;
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public TabAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
