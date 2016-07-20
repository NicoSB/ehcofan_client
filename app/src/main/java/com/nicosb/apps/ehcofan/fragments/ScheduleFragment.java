package com.nicosb.apps.ehcofan.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.nicosb.apps.ehcofan.R;
import com.nicosb.apps.ehcofan.models.Match;
import com.nicosb.apps.ehcofan.tasks.FetchMatchesTask;
import com.nicosb.apps.ehcofan.views.BottomRefreshScrollView;
import com.nicosb.apps.ehcofan.views.MatchView;

import java.util.ArrayList;


public class ScheduleFragment extends Fragment
                                implements FetchMatchesTask.OnScheduleFetchedListener{


    private static final String TAG = "ScheduleFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_schedule, container, false);
        setHasOptionsMenu(true);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        FetchMatchesTask fetchMatchesTask = new FetchMatchesTask(getContext());
        fetchMatchesTask.setOnScheduleFetchedListener(this);
        fetchMatchesTask.execute("EHCO Cup 2016");
    }

    @Override
    public void onScheduleFetched(ArrayList<Match> matches) {
        Log.d(TAG, "schedule fetched with " + matches.size() + " results");
        LinearLayout container = (LinearLayout)getActivity().findViewById(R.id.schedule_fragment_container);
        for(Match m: matches){
            MatchView mv = new MatchView(getContext(), m);
            container.addView(mv);
        }
    }
}
